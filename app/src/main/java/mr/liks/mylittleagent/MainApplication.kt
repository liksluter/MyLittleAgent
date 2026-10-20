package mr.liks.mylittleagent

import android.app.Application
import mr.liks.core.database.di.databaseModule
import mr.liks.feature.chat.impl.di.chatModule
import mr.liks.mylittleagent.di.appModule
import mr.liks.mylittleagent.llm.di.llmModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                appModule,
                databaseModule,
                llmModule,
                chatModule,
            )
        }
    }
}