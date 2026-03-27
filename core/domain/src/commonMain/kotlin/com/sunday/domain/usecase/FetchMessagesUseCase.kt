package com.sunday.domain.usecase

import com.sunday.domain.model.ConversationId
import com.sunday.domain.model.Message
import com.sunday.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class FetchMessagesUseCase(private val chatRepository: ChatRepository) {
    operator fun invoke(conversationId: ConversationId): Flow<FetchMessageState> {
        return chatRepository.streamMessages(conversationId)
            .map<List<Message>, FetchMessageState> { FetchMessageState.Success(it) }
            .onStart { emit(FetchMessageState.Loading) }
            .catch { cause -> emit(FetchMessageState.Error(cause)) }
        }
}

sealed interface FetchMessageState {
    data object Loading : FetchMessageState
    data class Success(val data: List<Message>) : FetchMessageState
    data class Error(val cause: Throwable): FetchMessageState
}