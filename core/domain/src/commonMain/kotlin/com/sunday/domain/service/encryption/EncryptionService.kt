package com.sunday.domain.service.encryption

import com.sunday.domain.util.DomainError
import com.sunday.domain.util.DomainResult
import com.sunday.domain.model.ConversationId

interface EncryptionService {

    suspend fun encryptText(
        plaintext: String,
        conversationId: ConversationId
    ): DomainResult<EncryptionResult, DomainError>

    suspend fun decryptText(
        ciphertext: ByteArray,
        nonce: ByteArray,
        conversationId: ConversationId
    ): DomainResult<String, DomainError>

}

data class EncryptionResult(
    val ciphertext: ByteArray,
    val nonce: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as EncryptionResult

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