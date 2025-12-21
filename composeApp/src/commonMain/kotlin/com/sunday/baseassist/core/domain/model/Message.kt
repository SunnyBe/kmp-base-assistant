package com.sunday.baseassist.core.domain.model

import kotlin.jvm.JvmInline
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

// Keep immutable
@OptIn(ExperimentalTime::class)
data class Message(
    val messageId: MessageId, // Indempotency Key, backend should treat as the same message.
    val participantId: ParticipantId,
    val serverId: ServerId? = null, // Indicate message is registered on the server
    val encryptedPayload: MessagePayload,
    val status: MessageStatus,
    val createdAt: Instant,
    val syncedAt: Instant? = null
)

@JvmInline
value class MessageId(val value: String)

@JvmInline
value class ServerId(val value: String)

@JvmInline
value class MessagePayload(val bytes: ByteArray)
