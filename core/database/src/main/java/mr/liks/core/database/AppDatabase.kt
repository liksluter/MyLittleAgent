package mr.liks.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import mr.liks.core.database.dao.ChatDao
import mr.liks.core.database.dao.MessageDao
import mr.liks.core.database.entity.ChatEntity
import mr.liks.core.database.entity.MessageEntity

@Database(
    entities = [
        ChatEntity::class,
        MessageEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao

    abstract fun messageDao(): MessageDao

    companion object {
        const val NAME = "app_database.db"
    }
}