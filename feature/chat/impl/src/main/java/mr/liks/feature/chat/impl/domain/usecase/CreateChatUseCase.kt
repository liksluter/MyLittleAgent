package mr.liks.feature.chat.impl.domain.usecase

import mr.liks.feature.chat.impl.domain.repository.ChatRepository

class CreateChatUseCase(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(): Long {
        val now = System.currentTimeMillis()
        return chatRepository.createChat(
            title = "New chat",
        )
    }
}