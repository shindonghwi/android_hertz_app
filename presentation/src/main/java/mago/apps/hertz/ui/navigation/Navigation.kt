package mago.apps.hertz.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import mago.apps.hertz.ui.model.screen.RouteScreen
import mago.apps.hertz.ui.navigation.answer.answerRouting
import mago.apps.hertz.ui.navigation.home.homeRouting
import mago.apps.hertz.ui.navigation.question.questionRouting

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = RouteScreen.HomeScreen.route) {

        navController.run {

            // 홈
            homeRouting(this)

            // 질문 관련
            questionRouting(this)

            // 답변 관련
            answerRouting(this)

        }

    }
}