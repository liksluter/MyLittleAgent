package mr.liks.mylittleagent.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import mr.liks.feature.chat.api.ChatNavKey
import mr.liks.feature.chat.impl.chatEntryBuilder

/** Главный NavDisplay приложения */
@Composable
fun AppNavDisplay() {
    val backStack = rememberNavBackStack(ChatNavKey)

    val entryProvider = entryProvider<NavKey> {
        chatEntryBuilder()
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider,
    )
}