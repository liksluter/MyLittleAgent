package mr.liks.mylittleagent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import mr.liks.mylittleagent.navigation.AppNavDisplay
import mr.liks.mylittleagent.ui.theme.MyLittleAgentTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyLittleAgentTheme {
                AppNavDisplay()
            }
        }
    }
}