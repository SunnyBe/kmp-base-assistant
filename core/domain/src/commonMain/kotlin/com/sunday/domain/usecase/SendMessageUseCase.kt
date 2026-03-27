package com.sunday.domain.usecase

import com.sunday.domain.util.DomainResult
import com.sunday.domain.model.ConversationId
import com.sunday.domain.model.MessagePayload
import com.sunday.domain.repository.ChatRepository
import com.sunday.domain.service.encryption.EncryptionService

class SendMessageUseCase(
    private val chatRepository: ChatRepository,
    private val encryptionService: EncryptionService
) {

    suspend operator fun invoke(
        conversationId: ConversationId,
        text: String
    ) {
        val encryptionResult = encryptionService.encryptText(text, conversationId)
        if (encryptionResult is DomainResult.Failed) return

        val encryptedPayload = encryptionResult as DomainResult.Success
        val payload = MessagePayload(
            bytes = encryptedPayload.data.ciphertext,
            nonce = encryptedPayload.data.nonce
        )
        chatRepository.sendMessage(conversationId, payload)
    }
}