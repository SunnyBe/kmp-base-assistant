package com.sunday.baseassist.core.domain.repository

import com.sunday.baseassist.core.domain.util.DomainError
import com.sunday.baseassist.core.domain.model.ConversationId
import com.sunday.baseassist.core.domain.model.MessageId
import com.sunday.baseassist.core.domain.model.Message
import com.sunday.baseassist.core.domain.model.MessagePayload
import com.sunday.baseassist.core.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun streamMessages(conversationId: ConversationId): Flow<List<Message>>
    suspend fun sendMessage(
        conversationId: ConversationId,
        payload: MessagePayload
    ): DomainResult<Unit, DomainError>

    suspend fun retryMessage(messageId: MessageId): DomainResult<Unit, DomainError>
}