package mr.liks.feature.chat.impl.domain.usecase

import kotlinx.coroutines.flow.Flow
import mr.liks.feature.chat.impl.domain.model.Chat
import mr.liks.feature.chat.impl.domain.repository.ChatRepository

class ObserveChatsUseCase(
    private val chatRepository: ChatRepository,
) {
    operator fun invoke(): Flow<List<Chat>> = chatRepository.observeChats()
}