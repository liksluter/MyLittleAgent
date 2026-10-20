package mr.liks.feature.chat.impl.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import mr.liks.feature.chat.impl.domain.model.Message
import mr.liks.feature.chat.impl.domain.repository.LlmRepository
import mr.liks.mylittleagent.llm.LiteRtLlmEngine
import mr.liks.mylittleagent.llm.LiteRtLlmSession

class LlmRepositoryImpl(
    private val llmEngine: LiteRtLlmEngine,
) : LlmRepository {
    private val sessionMutex = Mutex()
    private var activeSession: LiteRtLlmSession? = null
    private var activeChatId: Long? = null

    override fun generateResponse(
        chatId: Long,
        history: List<Message>,
        prompt: String,
    ): Flow<String> = flow {
        val session = sessionMutex.withLock {
            if (activeChatId != chatId) {
                activeSession?.close()
                activeSession = null
            }

            activeSession ?: llmEngine.createSession(
                history = history.map { it.text to it.isFromUser },
            ).also {
                activeSession = it
                activeChatId = chatId
            }
        }

        session.sendMessage(prompt).collect { chunk ->
            emit(chunk)
        }
    }

    override suspend fun closeSession() {
        sessionMutex.withLock {
            activeSession?.close()
            activeSession = null
            activeChatId = null
        }
    }
}