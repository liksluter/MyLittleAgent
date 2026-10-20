package mr.liks.feature.chat.impl.domain.usecase

import kotlinx.coroutines.flow.Flow
import mr.liks.feature.chat.impl.domain.model.Message
import mr.liks.feature.chat.impl.domain.repository.ChatRepository

class ObserveMessagesUseCase(
    private val chatRepository: ChatRepository,
) {
    operator fun invoke(chatId: Long): Flow<List<Message>> =
        chatRepository.observeMessages(chatId)
}