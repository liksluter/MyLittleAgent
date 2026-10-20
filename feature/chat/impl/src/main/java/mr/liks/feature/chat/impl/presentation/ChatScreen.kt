package mr.liks.feature.chat.impl.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import mr.liks.feature.chat.impl.domain.model.Chat
import mr.liks.feature.chat.impl.domain.model.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
) {
    val state by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                ChatUiEffect.ScrollToBottom -> {
                    val lastIndex = state.messages.size + if (state.isGenerating) 1 else 0
                    if (lastIndex > 0) {
                        listState.animateScrollToItem(lastIndex - 1)
                    }
                }
                is ChatUiEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ChatTopBar(
                chats = state.chats,
                activeChatId = state.activeChatId,
                onSelectChat = { viewModel.onIntent(ChatUiIntent.SelectChat(it)) },
                onCreateChat = { viewModel.onIntent(ChatUiIntent.CreateNewChat) },
            )
        },
        bottomBar = {
            ChatInputBar(
                text = state.inputText,
                canSend = state.canSend,
                onTextChange = { viewModel.onIntent(ChatUiIntent.InputChanged(it)) },
                onSend = { viewModel.onIntent(ChatUiIntent.SendMessage) },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingValues(vertical = 8.dp))
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = padding,
        ) {
            items(state.messages, key = { it.id }) { message ->
                MessageBubble(message = message)
            }
            if (state.isGenerating) {
                item(key = "streaming") {
                    StreamingBubble(text = state.streamingResponse)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatTopBar(
    chats: List<Chat>,
    activeChatId: Long?,
    onSelectChat: (Long) -> Unit,
    onCreateChat: () -> Unit,
) {
    TopAppBar(
        title = {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                item(key = "create") {
                    IconButton(onClick = onCreateChat) {
                        Icon(Icons.Default.Add, contentDescription = "New chat")
                    }
                }
                items(chats, key = { it.id }) { chat ->
                    FilterChip(
                        selected = chat.id == activeChatId,
                        onClick = { onSelectChat(chat.id) },
                        label = { Text(chat.title) },
                    )
                }
            }
        },
    )
}

@Composable
private fun MessageBubble(message: Message) {
    val alignment = if (message.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
    val containerColor = if (message.isFromUser) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    }
    val shape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (message.isFromUser) 16.dp else 4.dp,
        bottomEnd = if (message.isFromUser) 4.dp else 16.dp,
    )

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(shape)
                .background(containerColor)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
                .padding(12.dp),
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = formatTimestamp(message.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.End),
            )
        }
    }
}

@Composable
private fun StreamingBubble(text: String) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                .padding(12.dp),
        ) {
            Text(
                text = text.ifEmpty { "…" },
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun ChatInputBar(
    text: String,
    canSend: Boolean,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
) {
    Surface(
        tonalElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message…") },
                maxLines = 4,
                shape = RoundedCornerShape(24.dp),
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = onSend,
                enabled = canSend,
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date(timestamp))
}