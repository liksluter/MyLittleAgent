package mr.liks.feature.chat.impl.domain.repository

import kotlinx.coroutines.flow.Flow
import mr.liks.feature.chat.impl.domain.model.Chat
import mr.liks.feature.chat.impl.domain.model.Message

interface ChatRepository {
    fun observeChats(): Flow<List<Chat>>

    fun observeMessages(chatId: Long): Flow<List<Message>>

    suspend fun createChat(title: String): Long

    suspend fun saveMessage(message: Message): Long

    suspend fun updateChatTitle(chatId: Long, title: String)
}