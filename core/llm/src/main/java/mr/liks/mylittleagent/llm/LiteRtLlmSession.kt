package mr.liks.mylittleagent.llm

import com.google.ai.edge.litertlm.Content
import com.google.ai.edge.litertlm.Contents
import com.google.ai.edge.litertlm.Conversation
import com.google.ai.edge.litertlm.ConversationConfig
import com.google.ai.edge.litertlm.Engine
import com.google.ai.edge.litertlm.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mr.liks.mylittleagent.llm.model.LlmConfig

/** Сессия для одного чата */
class LiteRtLlmSession(
    engine: Engine,
    config: LlmConfig,
    history: List<Pair<String, Boolean>> = emptyList(),
) : AutoCloseable {
    private val conversation: Conversation

    init {
        val conversationConfig = buildConversationConfig(config, history)
        conversation = engine.createConversation(conversationConfig)
    }

    /**
     * Отправляет сообщение и возвращает Flow сгенерированного текста
     */
    fun sendMessage(text: String): Flow<String> = flow {
        conversation.sendMessageAsync(text).collect { message ->
            val chunk = extractText(message)
            if (chunk.isNotEmpty()) {
                emit(chunk)
            }
        }
    }

    override fun close() {
        conversation.close()
    }

    private fun buildConversationConfig(
        config: LlmConfig,
        history: List<Pair<String, Boolean>>,
    ): ConversationConfig {
        val systemInstruction = Contents.of(config.systemInstruction)

        val initialMessages = history.map { (text, isUser) ->
            if (isUser) Message.user(text) else Message.model(text)
        }

        return ConversationConfig(
            systemInstruction = systemInstruction,
            initialMessages = initialMessages,
        )
    }

    /**
     * Извлекает текстовый контент из стримингового Message
     */
    private fun extractText(message: Message): String {
        return message.contents.contents
            .filterIsInstance<Content.Text>()
            .joinToString("") { it.text }
    }
}