package mr.liks.feature.chat.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mr.liks.feature.chat.impl.domain.model.Message
import mr.liks.feature.chat.impl.domain.usecase.CreateChatUseCase
import mr.liks.feature.chat.impl.domain.usecase.ObserveChatsUseCase
import mr.liks.feature.chat.impl.domain.usecase.ObserveMessagesUseCase
import mr.liks.feature.chat.impl.domain.usecase.SendMessageUseCase

class ChatViewModel(
    private val observeChats: ObserveChatsUseCase,
    private val observeMessages: ObserveMessagesUseCase,
    private val createChat: CreateChatUseCase,
    private val sendMessage: SendMessageUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _effects = Channel<ChatUiEffect>(Channel.BUFFERED)
    val effects: Flow<ChatUiEffect> = _effects.receiveAsFlow()

    private var activeMessages: List<Message> = emptyList()

    init {
        observeChatsInternal()
    }

    fun onIntent(intent: ChatUiIntent) {
        when (intent) {
            is ChatUiIntent.InputChanged -> onInputChanged(intent.text)
            is ChatUiIntent.SelectChat -> onSelectChat(intent.chatId)
            ChatUiIntent.CreateNewChat -> onCreateNewChat()
            ChatUiIntent.SendMessage -> onSendMessage()
        }
    }

    private fun observeChatsInternal() {
        viewModelScope.launch {
            observeChats().collect { chats ->
                _uiState.update { state ->
                    val activeId = state.activeChatId ?: chats.firstOrNull()?.id
                    state.copy(chats = chats, activeChatId = activeId)
                }
                val activeId = _uiState.value.activeChatId
                if (activeId != null) {
                    observeMessagesInternal(activeId)
                }
            }
        }
    }

    private fun observeMessagesInternal(chatId: Long) {
        viewModelScope.launch {
            observeMessages(chatId).collect { messages ->
                activeMessages = messages
                _uiState.update { state ->
                    state.copy(messages = messages)
                }
            }
        }
    }

    private fun onInputChanged(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    private fun onSelectChat(chatId: Long) {
        if (_uiState.value.activeChatId == chatId) return
        _uiState.update {
            it.copy(
                activeChatId = chatId,
                messages = emptyList(),
                streamingResponse = "",
                isGenerating = false,
            )
        }
        observeMessagesInternal(chatId)
    }

    private fun onCreateNewChat() {
        viewModelScope.launch {
            val newChatId = createChat()
            _uiState.update {
                it.copy(
                    activeChatId = newChatId,
                    messages = emptyList(),
                    inputText = "",
                    streamingResponse = "",
                )
            }
            observeMessagesInternal(newChatId)
        }
    }

    private fun onSendMessage() {
        val state = _uiState.value
        if (!state.canSend) return

        val prompt = state.inputText
        val chatId = state.activeChatId ?: return
        val history = activeMessages

        _uiState.update {
            it.copy(
                inputText = "",
                isGenerating = true,
                streamingResponse = "",
                error = null,
            )
        }

        viewModelScope.launch {
            try {
                sendMessage(
                    chatId = chatId,
                    prompt = prompt,
                    history = history,
                ).collect { chunk ->
                    _uiState.update { s ->
                        s.copy(streamingResponse = s.streamingResponse + chunk)
                    }
                    _effects.trySend(ChatUiEffect.ScrollToBottom)
                }
                _uiState.update { it.copy(isGenerating = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isGenerating = false,
                        error = e.message,
                    )
                }
                _effects.trySend(
                    ChatUiEffect.ShowError(e.message ?: "Generation failed")
                )
            }
        }
    }
}