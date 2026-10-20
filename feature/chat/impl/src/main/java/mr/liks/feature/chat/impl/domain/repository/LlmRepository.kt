package mr.liks.feature.chat.impl.domain.repository

import kotlinx.coroutines.flow.Flow
import mr.liks.feature.chat.impl.domain.model.Message

interface LlmRepository {
    /**
     * Генерирует ответ модели с учётом истории чата
     */
    fun generateResponse(
        chatId: Long,
        history: List<Message>,
        prompt: String,
    ): Flow<String>

    /** Закрывает активную сессию при переключении чата */
    suspend fun closeSession()
}