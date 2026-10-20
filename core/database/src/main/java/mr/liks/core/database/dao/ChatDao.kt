package mr.liks.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import mr.liks.core.database.entity.ChatEntity

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<ChatEntity>>

    @Query("SELECT * FROM chats WHERE id = :chatId LIMIT 1")
    fun observeById(chatId: Long): Flow<ChatEntity?>

    @Insert
    suspend fun insert(chat: ChatEntity): Long

    @Query("UPDATE chats SET title = :title, updatedAt = :updatedAt WHERE id = :chatId")
    suspend fun updateTitle(chatId: Long, title: String, updatedAt: Long)

    @Query("UPDATE chats SET updatedAt = :updatedAt WHERE id = :chatId")
    suspend fun touch(chatId: Long, updatedAt: Long)

    @Query("DELETE FROM chats WHERE id = :chatId")
    suspend fun deleteById(chatId: Long)
}