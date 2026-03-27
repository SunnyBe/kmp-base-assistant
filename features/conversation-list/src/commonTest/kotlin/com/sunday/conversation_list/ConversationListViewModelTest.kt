package com.sunday.conversation_list

import app.cash.turbine.test
import com.sunday.domain.model.Conversation
import com.sunday.domain.model.ConversationId
import com.sunday.domain.model.Message
import com.sunday.domain.model.MessageId
import com.sunday.domain.model.MessagePayload
import com.sunday.domain.model.ParticipantId
import com.sunday.domain.repository.ChatRepository
import com.sunday.domain.service.formatter.DateFormatter
import com.sunday.domain.util.DomainError
import com.sunday.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class ConversationListViewModelTest {

    private lateinit var chatRepository: ChatRepository
    private lateinit var dateFormatter: DateFormatter
    private lateinit var viewModel: ConversationListViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val fakeConversation = FakeChatRepository.fakeConversations

    @BeforeTest
    fun setup() {
        chatRepository = FakeChatRepository()
        dateFormatter = FakeDateFormatter()
        viewModel = ConversationListViewModel(chatRepository, dateFormatter)
    }

    @Test
    fun `initial state is loading`() = testScope.runTest {
        assertTrue(viewModel.state.value.isLoading)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `state has a valid list conversation`() = testScope.runTest {
        val expectedConversations = fakeConversation.map { conversation ->
            UIConversation(
                id = conversation.id.value,
                preview = conversation.lastMessageSnippet ?: "",
                participant = conversation.participantName,
                unReadCount = conversation.unreadCount,
                lastUpdated = conversation.updatedAt.toString(),
                participantAvatar = "https://www.gbese.com/cloud"
            )
        }
        viewModel = ConversationListViewModel(
            FakeChatRepository(conversations = fakeConversation),
            FakeDateFormatter()
        )

        viewModel.state.test {
            awaitItem() // Initial state(Loading)
            val state = awaitItem()
            assertEquals(expectedConversations, state.conversations)

            cancelAndIgnoreRemainingEvents()
        }
    }
}

internal class FakeChatRepository(
    private val conversations: List<Conversation> = emptyList(),
    private val messages: List<Message> = emptyList()
) : ChatRepository {
    override fun streamConversations(): Flow<List<Conversation>> = flowOf(conversations)

    override fun streamMessages(conversationId: ConversationId): Flow<List<Message>> =
        flowOf(messages)

    override suspend fun sendMessage(
        conversationId: ConversationId,
        payload: MessagePayload
    ): DomainResult<MessageId, DomainError> {
        TODO("Not yet implemented")
    }

    override suspend fun retryMessage(messageId: MessageId): DomainResult<MessageId, DomainError> {
        TODO("Not yet implemented")
    }

    override suspend fun syncMessage(messageId: MessageId): DomainResult<MessageId, DomainError> {
        TODO("Not yet implemented")
    }

    companion object {
        @OptIn(ExperimentalTime::class)
        val fakeConversations = buildList {
            repeat(5) {
                add(
                    Conversation(
                        id = ConversationId("fake-id-$it"),
                        participantId = ParticipantId("peer-$it"),
                        lastMessageSnippet = "I see dumb niggas",
                        unreadCount = 5,
                        updatedAt = Instant.parse("2025-01-10T14:0$it:00Z"),
                        participantName = "Fake User $it",
                        participantAvatar = "https://www.gbese.com/cloud"
                    )
                )
            }
        }
    }

}

internal class FakeDateFormatter : DateFormatter {
    @OptIn(ExperimentalTime::class)
    override fun format(instant: Instant): String = instant.toString()

}