package com.sunday.domain.repository

import com.sunday.domain.util.DomainError
import com.sunday.domain.util.DomainResult
import com.sunday.domain.model.Conversation
import com.sunday.domain.model.ConversationId
import com.sunday.domain.model.Message
import com.sunday.domain.model.MessageId
import com.sunday.domain.model.MessagePayload
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun streamConversations(): Flow<List<Conversation>>
    fun streamMessages(conversationId: ConversationId): Flow<List<Message>>
    suspend fun sendMessage(
        conversationId: ConversationId,
        payload: MessagePayload
    ): DomainResult<MessageId, DomainError>

    suspend fun retryMessage(messageId: MessageId): DomainResult<MessageId, DomainError>
    suspend fun syncMessage(messageId: MessageId): DomainResult<MessageId, DomainError>
}