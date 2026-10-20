package mr.liks.feature.chat.impl

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import mr.liks.feature.chat.api.ChatNavKey
import mr.liks.feature.chat.impl.presentation.ChatScreen
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.chatEntryBuilder() {
    entry<ChatNavKey> {
        ChatScreen(
            viewModel = koinViewModel(),
        )
    }
}