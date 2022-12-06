package mago.apps.hertz.ui.components.bottombar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import mago.apps.hertz.ui.navigation.model.RouteScreen
import mago.apps.hertz.ui.components.bottombar.question.QuestionBottomBar

@Composable
fun BottomBar(currentRoute: String?, navController: NavHostController) {
    currentRoute?.let { route ->
        when (route) {
            RouteScreen.QuestionScreen.route -> {
                QuestionBottomBar(navController = navController)
            }
        }
    }
}

