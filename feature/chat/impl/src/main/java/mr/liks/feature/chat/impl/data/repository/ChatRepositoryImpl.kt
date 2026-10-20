package mr.liks.feature.chat.impl.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mr.liks.core.database.dao.ChatDao
import mr.liks.core.database.dao.MessageDao
import mr.liks.core.database.entity.ChatEntity
import mr.liks.feature.chat.impl.data.mapper.toDomain
import mr.liks.feature.chat.impl.data.mapper.toEntity
import mr.liks.feature.chat.impl.domain.model.Chat
import mr.liks.feature.chat.impl.domain.model.Message
import mr.liks.feature.chat.impl.domain.repository.ChatRepository

class ChatRepositoryImpl(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao,
) : ChatRepository {

    override fun observeChats(): Flow<List<Chat>> =
        chatDao.observeAll().map { entities -> entities.map(ChatEntity::toDomain) }

    override fun observeMessages(chatId: Long): Flow<List<Message>> =
        messageDao.observeByChat(chatId).map { entities -> entities.map { it.toDomain() } }

    override suspend fun createChat(title: String): Long {
        val now = System.currentTimeMillis()
        return chatDao.insert(
            ChatEntity(
                title = title,
                createdAt = now,
                updatedAt = now,
            )
        )
    }

    override suspend fun saveMessage(message: Message): Long {
        val id = messageDao.insert(message.toEntity())
        chatDao.touch(message.chatId, message.timestamp)
        return id
    }

    override suspend fun updateChatTitle(chatId: Long, title: String) {
        chatDao.updateTitle(chatId, title, System.currentTimeMillis())
    }
}