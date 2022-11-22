package mago.apps.hertz

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import mago.apps.hertz.ui.navigation.Navigation
import mago.apps.hertz.ui.theme.MagoHzTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MagoHzTheme {
                Navigation()
            }
        }
    }
}