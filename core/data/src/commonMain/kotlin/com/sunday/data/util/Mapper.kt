package com.sunday.data.util

import com.sunday.data.local.GetConversationList
import com.sunday.data.local.MessageEntity
import com.sunday.domain.model.Conversation
import com.sunday.domain.model.Message
import com.sunday.domain.model.MessagePayload
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal fun MessageEntity.toDomain() = Message(
    id = localId,
    senderId = senderId,
    serverId = serverId,
    encryptedPayload = MessagePayload(encryptedPayload, nonce),
    status = status,
    createdAt = Instant.fromEpochMilliseconds(createdAt),
    syncedAt = syncedAt?.let { Instant.fromEpochMilliseconds(syncedAt) }
)

// Conversation
@OptIn(ExperimentalTime::class)
internal fun GetConversationList.toDomain() = Conversation(
    id = id,
    participantId = peerId,
    lastMessageSnippet = lastMessageSnippet,
    unreadCount = unreadCount,
    updatedAt = Instant.fromEpochMilliseconds(updatedAt),
    participantName = peerName ?: "",
    participantAvatar = peerAvatar
)
