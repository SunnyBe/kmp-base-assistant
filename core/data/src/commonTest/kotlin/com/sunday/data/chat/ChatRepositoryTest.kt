package com.sunday.data.repository

import app.cash.turbine.test
import com.sunday.data.fakes.FakeAccountService
import com.sunday.data.fakes.FakeChatApiService
import com.sunday.data.fakes.FakeConnectivityService
import com.sunday.data.local.ChatDatabase
import com.sunday.data.local.ConversationEntity
import com.sunday.data.utils.conversationEntityAdapter
import com.sunday.data.utils.createTestSqlDriver
import com.sunday.data.utils.messageEntityAdapter
import com.sunday.data.utils.participantEntityAdapter
import com.sunday.domain.model.asConversionId
import com.sunday.domain.model.asParticipantId
import com.sunday.domain.repository.ChatRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ChatRepositoryTest {

    private lateinit var database: ChatDatabase
    private lateinit var repository: ChatRepository

    @BeforeTest
    fun setup() {
        val driver = createTestSqlDriver()
        database = ChatDatabase(
            driver = driver,
            ConversationEntityAdapter = conversationEntityAdapter,
            MessageEntityAdapter = messageEntityAdapter,
            ParticipantEntityAdapter = participantEntityAdapter
        )
    }

    @Test
    fun `when streamConversations is called, it emits cached items immediately`() = runTest {
        // Arrange: Seed the database
        val queries = database.chatDatabaseQueries
        val timestamp = 1710000000000L

        val testEntities = (0..2).map { i ->
            ConversationEntity(
                id = "conv-$i".asConversionId(), // Use simple strings or your .asId() helpers
                peerId = "peer-$i".asParticipantId(),
                lastMessageSnippet = "Message $i",
                unreadCount = i.toLong(),
                updatedAt = timestamp
            )
        }

        testEntities.forEach { queries.upsertConversation(it) }

        // Initialize repository with the SAME testScheduler
        // Using UnconfinedTestDispatcher makes the Flow collection synchronous for the test
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)

        repository = ChatRepositoryImpl(
            database = database,
            network = FakeChatApiService(),
            accountService = FakeAccountService(),
            connectivityService = FakeConnectivityService(),
            coroutineScope = backgroundScope, // Provided by runTest
            ioDispatcher = testDispatcher
        )

        repository.streamConversations().test {
            val items = awaitItem()

            assertEquals(3, items.size, "Repository should emit all 3 cached items")
            assertEquals("conv-0", items[0].id.value)
            assertEquals("Message 0", items[0].lastMessageSnippet)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when database is updated, streamConversations emits new list automatically`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        repository = ChatRepositoryImpl(database,
            FakeChatApiService(), FakeAccountService(), FakeConnectivityService(), backgroundScope, testDispatcher)

        repository.streamConversations().test {
            // Initial emission should be empty
            assertEquals(0, awaitItem().size)

            // Act: Insert data while the flow is being collected
            database.chatDatabaseQueries.upsertConversation(
                ConversationEntity("new-id".asConversionId(), "peer-1".asParticipantId(), "Hello", 0, 123L)
            )

            // Assert: The flow should react to the DB change
            val updatedItems = awaitItem()
            assertEquals(1, updatedItems.size)
            assertEquals("new-id", updatedItems[0].id.value)

            cancelAndIgnoreRemainingEvents()
        }
    }
}