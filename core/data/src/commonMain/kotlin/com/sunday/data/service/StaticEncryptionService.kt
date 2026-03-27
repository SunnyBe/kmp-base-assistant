package com.sunday.data.service

import com.sunday.domain.model.ConversationId
import com.sunday.domain.service.encryption.EncryptionResult
import com.sunday.domain.service.encryption.EncryptionService
import com.sunday.domain.util.DomainError
import com.sunday.domain.util.DomainResult

class StaticEncryptionService : EncryptionService {
    // A hardcoded 32-byte key for AES (just for now)
    private val staticKey = "this-is-a-32-byte-long-secret-key!!".encodeToByteArray()

    override suspend fun encryptText(
        plaintext: String,
        conversationId: ConversationId
    ): DomainResult<EncryptionResult, DomainError> {
        // TODO: Replace with real X3DH/Double Ratchet handshake logic
        // For now, we just return the bytes of the string
        return DomainResult.Success(
            EncryptionResult(
                ciphertext = plaintext.encodeToByteArray(),
                nonce = "static-nonce".encodeToByteArray()
            )
        )
    }

    override suspend fun decryptText(
        ciphertext: ByteArray,
        nonce: ByteArray,
        conversationId: ConversationId
    ): DomainResult<String, DomainError> {
        // TODO: Perform actual decryption using derived session keys
        return DomainResult.Success(ciphertext.decodeToString())
    }
}