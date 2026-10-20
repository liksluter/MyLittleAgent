package mr.liks.feature.chat.impl.domain.model

data class Message(
    val id: Long,
    val chatId: Long,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long,
)