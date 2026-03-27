package com.sunday.data.source.network

import com.sunday.domain.model.MessageId
import com.sunday.domain.model.MessageStatus
import com.sunday.domain.model.ParticipantId
import com.sunday.domain.model.ServerId

interface ChatApiService {
    suspend fun pushMessage(messageDto: MessageRequestDto): MessageResponseDto

}

data class MessageRequestDto(
    val messageId: MessageId,
    val senderId: ParticipantId,
    val serverId: ServerId? = null,
    val encryptedPayload: MessagePayloadDto,
    val status: MessageStatus,
    val createdAt: String,
    val syncedAt: String? = null
)

data class MessagePayloadDto(
    val ciphertext: ByteArray,
    val nonce: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as MessagePayloadDto

        if (!ciphertext.contentEquals(other.ciphertext)) return false
        if (!nonce.contentEquals(other.nonce)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ciphertext.contentHashCode()
        result = 31 * result + nonce.contentHashCode()
        return result
    }
}

data class MessageResponseDto(
    val id: MessageId,
    val serverId: ServerId,
    val syncedAt: String // ISO Date format
)

