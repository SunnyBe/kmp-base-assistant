package com.sunday.conversation_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunday.domain.model.ConversationId
import com.sunday.domain.repository.ChatRepository
import com.sunday.domain.service.formatter.DateFormatter
import com.sunday.ui_kit.generated.resources.Res
import com.sunday.ui_kit.generated.resources.error_something_unexpected_label
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

class ConversationListViewModel(
    private val repository: ChatRepository,
    private val dateFormatter: DateFormatter
) : ViewModel() {

    @OptIn(ExperimentalTime::class)
    val state: StateFlow<ConversationListState> = repository.streamConversations()
        .map { conversations ->
            val uiConversations = conversations.map { conversation ->
                UIConversation(
                    id = conversation.id.value,
                    preview = conversation.lastMessageSnippet ?: "",
                    participantAvatar = conversation.participantAvatar,
                    participant = conversation.participantName,
                    unReadCount = conversation.unreadCount,
                    lastUpdated = dateFormatter.format(conversation.updatedAt)
                )
            }
            ConversationListState(conversations = uiConversations)
        }
        .catch { cause ->
            val messageRes = when (cause) {
                else -> Res.string.error_something_unexpected_label
            }
            ConversationListState(errorRes = messageRes)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ConversationListState(isLoading = true)
        )

    private val _effect = MutableSharedFlow<ConversationListEffect>()
    val effect = _effect.asSharedFlow()

    init {
        observeConversations()
    }

    @OptIn(ExperimentalTime::class)
    private fun observeConversations() {

    }

    fun onConversationClick(id: String) {
        viewModelScope.launch {
            _effect.tryEmit(ConversationListEffect.NavigateToChat(ConversationId(id)))
        }
    }

    fun onDismissErrorDialog() {
        viewModelScope.launch {
            _effect.tryEmit(ConversationListEffect.Dismiss)
        }
    }

    fun onRetryFetch() {
        viewModelScope.launch {
            _effect.tryEmit(ConversationListEffect.RetryConversationFetch)
        }
    }
}