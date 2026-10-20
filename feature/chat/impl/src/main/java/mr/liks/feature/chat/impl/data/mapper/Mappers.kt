package mr.liks.feature.chat.impl.data.mapper

import mr.liks.core.database.entity.ChatEntity
import mr.liks.core.database.entity.MessageEntity
import mr.liks.feature.chat.impl.domain.model.Chat
import mr.liks.feature.chat.impl.domain.model.Message

fun ChatEntity.toDomain(): Chat = Chat(
    id = id,
    title = title,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun MessageEntity.toDomain(): Message = Message(
    id = id,
    chatId = chatId,
    text = text,
    isFromUser = isFromUser,
    timestamp = timestamp,
)

fun Message.toEntity(): MessageEntity = MessageEntity(
    id = id,
    chatId = chatId,
    text = text,
    isFromUser = isFromUser,
    timestamp = timestamp,
)