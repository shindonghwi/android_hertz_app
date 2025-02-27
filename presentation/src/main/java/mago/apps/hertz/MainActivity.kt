package mago.apps.hertz

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import mago.apps.hertz.ui.model.screen.RouteScreen
import mago.apps.hertz.ui.navigation.Navigation
import mago.apps.hertz.ui.navigation.navigateTo
import mago.apps.hertz.ui.theme.MagoHzTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val sharedViewModel by viewModels<SharedViewModel>()
    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            MagoHzTheme {
                MainApp(navController)
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val newOverride = Configuration(newBase?.resources?.configuration)
        if (newOverride.fontScale >= 1.1f)
            newOverride.fontScale = 1.1f
        applyOverrideConfiguration(newOverride)
        super.attachBaseContext(newBase)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        processIntent(intent)
    }

    private fun processIntent(intent: Intent?) {
        if (intent != null) {
            val linkUrl = intent.getStringExtra("linkUrl")
            Log.w("ASdasdasd", "processIntent: $linkUrl")
            if (!linkUrl.isNullOrEmpty()) {

                if (linkUrl.contains(RouteScreen.QuestionScreen.route)) {
                    val questionSeq = linkUrl.replace(RouteScreen.QuestionScreen.route, "")
                    navController.navigateTo(
                        RouteScreen.QuestionScreen.route +
                                "?questionSeq=${questionSeq}"
                    )
                } else if (linkUrl.contains(RouteScreen.AnswerConnectedScreen.route)) {
                    val answerSeq = linkUrl.replace(RouteScreen.AnswerConnectedScreen.route, "")
                    navController.navigateTo(
                        RouteScreen.AnswerConnectedScreen.route +
                                "?answerSeq=${answerSeq}"
                    )
                }
            }
        }
    }


}


@Composable
private fun MainApp(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Navigation(navController)
    }
}