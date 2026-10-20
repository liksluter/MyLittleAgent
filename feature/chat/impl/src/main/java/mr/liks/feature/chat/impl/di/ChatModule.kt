package mr.liks.feature.chat.impl.di

import mr.liks.feature.chat.impl.data.repository.ChatRepositoryImpl
import mr.liks.feature.chat.impl.data.repository.LlmRepositoryImpl
import mr.liks.feature.chat.impl.domain.repository.ChatRepository
import mr.liks.feature.chat.impl.domain.repository.LlmRepository
import mr.liks.feature.chat.impl.domain.usecase.CreateChatUseCase
import mr.liks.feature.chat.impl.domain.usecase.ObserveChatsUseCase
import mr.liks.feature.chat.impl.domain.usecase.ObserveMessagesUseCase
import mr.liks.feature.chat.impl.domain.usecase.SendMessageUseCase
import mr.liks.feature.chat.impl.presentation.ChatViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val chatModule = module {
    single<ChatRepository> { ChatRepositoryImpl(get(), get()) }
    single<LlmRepository> { LlmRepositoryImpl(get()) }

    factory { ObserveChatsUseCase(get()) }
    factory { ObserveMessagesUseCase(get()) }
    factory { CreateChatUseCase(get()) }
    factory { SendMessageUseCase(get(), get()) }

    viewModel {
        ChatViewModel(
            observeChats = get(),
            observeMessages = get(),
            createChat = get(),
            sendMessage = get(),
        )
    }
}