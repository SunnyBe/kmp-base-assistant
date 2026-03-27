package com.sunday.conversation_list

import androidx.compose.runtime.Immutable
import com.sunday.domain.model.ConversationId
import org.jetbrains.compose.resources.StringResource

data class ConversationListState(
    val conversations: List<UIConversation> = emptyList(),
    val isLoading: Boolean = false,
    val errorRes: StringResource? = null
)

@Immutable
data class UIConversation(
    val id: String,
    val preview: String,
    val participantAvatar: String?,
    val participant: String,
    val unReadCount: Long,
    val lastUpdated: String
)

sealed interface ConversationListEffect {
    data class NavigateToChat(val conversationId: ConversationId) : ConversationListEffect
    data class ShowError(val message: String) : ConversationListEffect
    data object RetryConversationFetch : ConversationListEffect
    data object Dismiss : ConversationListEffect
}
