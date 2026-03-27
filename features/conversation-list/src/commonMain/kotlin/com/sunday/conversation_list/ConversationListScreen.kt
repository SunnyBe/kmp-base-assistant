package com.sunday.conversation_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sunday.ui_kit.generated.resources.Res
import com.sunday.ui_kit.component.ErrorContent
import com.sunday.ui_kit.component.LoadingContent
import com.sunday.ui_kit.generated.resources.add_circle_24dp
import com.sunday.ui_kit.generated.resources.inbox_title_label
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.random.Random
import kotlin.time.ExperimentalTime

@Composable
fun ConversationListScreen(viewModel: ConversationListViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ConversationListEffect.NavigateToChat -> TODO()
                is ConversationListEffect.ShowError -> TODO()
                ConversationListEffect.Dismiss -> TODO()
                ConversationListEffect.RetryConversationFetch -> TODO()
            }
        }
    }

    ConversationListContent(
        state = state,
        onSelectConversation = viewModel::onConversationClick,
        onRetryFetch = viewModel::onRetryFetch,
        onDismiss = viewModel::onDismissErrorDialog
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationListContent(
    state: ConversationListState,
    onSelectConversation: (id: String) -> Unit,
    onRetryFetch: () -> Unit,
    onDismiss: () -> Unit
) {

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(Res.string.inbox_title_label)) }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Start new conversation */ }) {
                // In a real app, load an appropriate icon
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when {
                state.isLoading -> LoadingContent(Modifier.align(Alignment.Center))
                state.errorRes != null -> ErrorContent(state.errorRes, onDismiss)
                state.conversations.isEmpty() -> EmptyStateContent()
                else -> ConversationLazyList(
                    conversations = state.conversations,
                    onSelectConversation = onSelectConversation,
                )
            }
        }
    }
}

@Composable
fun ConversationLazyList(
    conversations: List<UIConversation>,
    onSelectConversation: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(conversations, key = { it.id }) { conversation ->
            ConversationItem(conversation, onSelectConversation)
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@Composable
fun ConversationItem(
    item: UIConversation,
    onSelectConversation: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(48.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                ConversationAvatar(item.participantAvatar, item.participant.take(2))
            }
        }

        Column(
            modifier = Modifier.clickable(onClick = { onSelectConversation(item.id) }),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.75f),
                text = item.participant,
                style = MaterialTheme.typography.titleMedium
            )

            Row {
                if (false) {
                    Icon(
                        modifier = Modifier.size(10.dp).align(Alignment.CenterVertically)
                            .padding(2.dp),
                        painter = painterResource(Res.drawable.add_circle_24dp),
                        contentDescription = null
                    )
                }
                Text(
                    modifier = Modifier.fillMaxWidth(0.75f),
                    text = item.preview,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            modifier = Modifier,
            text = item.lastUpdated,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun ConversationAvatar(url: String?, placeholderText: String?) {
    url?.let {
        Image(
            painter = painterResource(Res.drawable.add_circle_24dp),
            contentDescription = null
        )
    } ?: run {
        Text(
            text = placeholderText?.uppercase() ?: "",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun EmptyStateContent() {
    // Display empty state UI with a retry button
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No conversations available.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
fun ConversationItemPreview() {
    MaterialTheme {
        ConversationItem(
            UIConversation(
                id = "test-01",
                preview = "Go to Hell",
                participantAvatar = null,
                participant = "Mikel Owen",
                unReadCount = 30,
                lastUpdated = "3 mins",
            ),
            onSelectConversation = {}
        )
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
fun ConversationListContentPreview() {
    val state = ConversationListState(
        conversations = buildList {
            repeat(10) { index ->
                add(
                    UIConversation(
                        id = "test-$index",
                        preview = "This is a preview message for conversation $index",
                        participantAvatar = null,
                        participant = "Participant $index",
                        unReadCount = Random.nextLong(0, 100),
                        lastUpdated = "${Random.nextInt(1, 59)} mins",
                    )
                )
            }
        },
        isLoading = false,
        errorRes = null
    )
    MaterialTheme {
        ConversationListContent(
            state = state,
            onSelectConversation = {},
            onRetryFetch = {},
            onDismiss = {})
    }
}