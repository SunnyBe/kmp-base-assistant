package com.sunday.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.sunday.data.local.ChatDatabase
import com.sunday.data.local.MessageEntity
import com.sunday.data.source.network.ChatApiService
import com.sunday.data.source.network.MessagePayloadDto
import com.sunday.data.source.network.MessageRequestDto
import com.sunday.data.util.toDomain
import com.sunday.domain.model.Conversation
import com.sunday.domain.model.ConversationId
import com.sunday.domain.model.Message
import com.sunday.domain.model.MessageId
import com.sunday.domain.model.MessagePayload
import com.sunday.domain.model.MessageStatus
import com.sunday.domain.repository.ChatRepository
import com.sunday.domain.service.account.AccountService
import com.sunday.domain.service.connectivity.ConnectivityService
import com.sunday.domain.util.DomainError
import com.sunday.domain.util.DomainResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatRepositoryImpl(
    private val database: ChatDatabase,
    private val network: ChatApiService,
    private val accountService: AccountService,
    private val connectivityService: ConnectivityService,
    private val coroutineScope: CoroutineScope, // Outlives the UI scope
    private val ioDispatcher: CoroutineDispatcher
) : ChatRepository {

    private val activeSyncs = mutableSetOf<MessageId>()

    private val queries = database.chatDatabaseQueries

    init {
        startSyncWatcher()
    }

    override fun streamConversations(): Flow<List<Conversation>> {
        return queries.getConversationList().asFlow().mapToList(ioDispatcher)
            .map { conversations -> conversations.map { it.toDomain() } }
    }

    override fun streamMessages(conversationId: ConversationId): Flow<List<Message>> {
        return queries.getMessagesForConversation(conversationId)
            .asFlow()
            .mapToList(ioDispatcher)
            .map { entities -> entities.map { it.toDomain() } }
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    override suspend fun sendMessage(
        conversationId: ConversationId,
        payload: MessagePayload
    ): DomainResult<MessageId, DomainError> = withContext(ioDispatcher) {
        val messageId = MessageId(Uuid.random().toString())
        val currentUserid =
            accountService.getCurrentUserId() ?: return@withContext DomainResult.Failed(
                DomainError.Unauthorized
            )
        try {
            database.transaction {
                MessageEntity(
                    localId = messageId,
                    serverId = null,
                    senderId = currentUserid,
                    conversationId = conversationId,
                    encryptedPayload = payload.bytes,
                    nonce = payload.nonce,
                    status = MessageStatus.Sending,
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    syncedAt = null
                ).let {
                    queries.insertMessage(it)
                }
            }
            DomainResult.Success(messageId)
        } catch (cause: Exception) {
            // Log the error
            DomainResult.Failed(DomainError.Unknown(cause.message ?: "Unknown"))
        }
    }

    override suspend fun retryMessage(messageId: MessageId): DomainResult<MessageId, DomainError> {
        return syncMessage(messageId)
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun syncMessage(messageId: MessageId): DomainResult<MessageId, DomainError> {
        try {
            if (!activeSyncs.add(messageId)) return DomainResult.Success(messageId) // models acquiring lock

            val entity = queries.getMessageById(messageId).executeAsOneOrNull()
                ?: return DomainResult.Failed(DomainError.MessageUnavailable)
            if (entity.status != MessageStatus.Sending) return DomainResult.Success(entity.localId)

            val result = network.pushMessage(entity.toNetworkDto())

            // Only update db when network is successful
            database.transaction {
                queries.updateMessageStatus(
                    MessageStatus.Sent,
                    serverId = result.serverId,
                    syncedAt = Instant.parse(result.syncedAt).toEpochMilliseconds(),
                    localId = messageId
                )
            }

            return DomainResult.Success(result.id)
        } catch (cause: Exception) {
            // TODO map error properly later.
            return DomainResult.Failed(DomainError.Unknown(cause.message ?: "Unknown"))
        } finally {
            activeSyncs.remove(messageId) // Models lock release
        }
    }

    private fun startSyncWatcher() {
        combine(
            queries.getPendingMessages().asFlow().mapToList(ioDispatcher),
            connectivityService.isOnline()
        ) { pending, isOnline ->
            if (isOnline && pending.isNotEmpty()) {
                pending.chunked(CHUNK_SIZE).map { chunkedPending ->
                    chunkedPending.forEach { messageEntity ->
                        coroutineScope.launch { syncMessage(messageEntity.localId) }
                    }
                }
            }
        }.launchIn(coroutineScope)
    }

    @OptIn(ExperimentalTime::class)
    private fun MessageEntity.toNetworkDto() = MessageRequestDto(
        messageId = localId,
        senderId = senderId,
        serverId = null,
        encryptedPayload = MessagePayloadDto(encryptedPayload, nonce),
        status = status,
        createdAt = createdAt.toString(),
        syncedAt = null,
    )

    companion object {
        private const val CHUNK_SIZE = 10
    }
}