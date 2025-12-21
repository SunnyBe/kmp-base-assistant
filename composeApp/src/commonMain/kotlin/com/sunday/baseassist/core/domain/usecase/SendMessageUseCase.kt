package com.sunday.baseassist.core.domain.usecase

import com.sunday.baseassist.core.domain.model.ConversationId
import com.sunday.baseassist.core.domain.model.MessagePayload
import com.sunday.baseassist.core.domain.repository.ChatRepository

class SendMessageUseCase(val chatRepository: ChatRepository) {

    suspend operator fun invoke(conversationId: ConversationId, payload: MessagePayload) {
        chatRepository.sendMessage(conversationId, payload)
    }
}