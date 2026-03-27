package com.sunday.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class Message(
    val id: MessageId,
    val senderId: ParticipantId,
    val serverId: ServerId? = null, // Indicate message is registered on the server
    val encryptedPayload: MessagePayload,
    val status: MessageStatus,
    val createdAt: Instant,
    val syncedAt: Instant? = null
)

data class MessagePayload(val bytes: ByteArray, val nonce: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as MessagePayload

        if (!bytes.contentEquals(other.bytes)) return false
        if (!nonce.contentEquals(other.nonce)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + nonce.contentHashCode()
        return result
    }
}
