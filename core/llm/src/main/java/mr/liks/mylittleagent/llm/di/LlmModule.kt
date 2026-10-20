package mr.liks.mylittleagent.llm.di

import mr.liks.mylittleagent.llm.LiteRtLlmEngine
import mr.liks.mylittleagent.llm.ModelFileManager
import mr.liks.mylittleagent.llm.model.LlmConfig
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val llmModule = module {
    single { LlmConfig() }

    single { ModelFileManager(androidContext()) }

    single {
        LiteRtLlmEngine(
            context = androidContext(),
            config = get(),
            modelFileManager = get(),
        )
    }
}