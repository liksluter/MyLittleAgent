package mr.liks.core.database.di

import androidx.room.Room
import mr.liks.core.database.AppDatabase
import mr.liks.core.database.dao.ChatDao
import mr.liks.core.database.dao.MessageDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import kotlin.jvm.java

val databaseModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = AppDatabase::class.java,
            name = AppDatabase.NAME,
        )
            .fallbackToDestructiveMigration() // todo жо стабилизации MVP
            .build()
    }

    single<ChatDao> { get<AppDatabase>().chatDao() }
    single<MessageDao> { get<AppDatabase>().messageDao() }
}