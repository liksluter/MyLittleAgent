package mr.liks.feature.chat.impl.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mr.liks.feature.chat.impl.domain.model.Message
import mr.liks.feature.chat.impl.domain.repository.ChatRepository
import mr.liks.feature.chat.impl.domain.repository.LlmRepository

class SendMessageUseCase(
    private val chatRepository: ChatRepository,
    private val llmRepository: LlmRepository,
) {
    /**
     * @param chatId  текущий чат
     * @param prompt  текст от пользователя
     * @param history предыдущие сообщения чата (для контекста модели)
     * @return Flow<String> — стриминг ответа модели
     */
    operator fun invoke(
        chatId: Long,
        prompt: String,
        history: List<Message>,
    ): Flow<String> = flow {
        val now = System.currentTimeMillis()

        chatRepository.saveMessage(
            Message(
                id = 0L,
                chatId = chatId,
                text = prompt,
                isFromUser = true,
                timestamp = now,
            )
        )

        val fullResponse = StringBuilder()
        llmRepository.generateResponse(
            chatId = chatId,
            history = history,
            prompt = prompt,
        ).collect { chunk ->
            fullResponse.append(chunk)
            emit(chunk)
        }

        chatRepository.saveMessage(
            Message(
                id = 0L,
                chatId = chatId,
                text = fullResponse.toString(),
                isFromUser = false,
                timestamp = System.currentTimeMillis(),
            )
        )
    }
}