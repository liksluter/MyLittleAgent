package mr.liks.feature.chat.impl.presentation

import mr.liks.feature.chat.impl.domain.model.Chat
import mr.liks.feature.chat.impl.domain.model.Message

data class ChatUiState(
    val chats: List<Chat> = emptyList(),
    val activeChatId: Long? = null,
    val messages: List<Message> = emptyList(),
    val inputText: String = "",
    val isGenerating: Boolean = false,
    val streamingResponse: String = "",
    val error: String? = null,
) {
    val canSend: Boolean
        get() = inputText.isNotBlank() && activeChatId != null && !isGenerating
}

sealed interface ChatUiIntent {
    data class InputChanged(val text: String) : ChatUiIntent
    data class SelectChat(val chatId: Long) : ChatUiIntent
    data object CreateNewChat : ChatUiIntent
    data object SendMessage : ChatUiIntent
}

sealed interface ChatUiEffect {
    data object ScrollToBottom : ChatUiEffect
    data class ShowError(val message: String) : ChatUiEffect
}