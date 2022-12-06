package mago.apps.hertz.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import mago.apps.hertz.ui.navigation.model.RouteScreen
import mago.apps.hertz.ui.screens.answer.audio.AnswerAudioScreen
import mago.apps.hertz.ui.screens.category.CategoryScreen
import mago.apps.hertz.ui.screens.home.HomeScreen
import mago.apps.hertz.ui.screens.question.QuestionScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = RouteScreen.HomeScreen.route) {
        composable(route = RouteScreen.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(route = RouteScreen.QuestionScreen.route) {
            QuestionScreen(navController)
        }
        composable(route = RouteScreen.CategoryScreen.route) {
            CategoryScreen()
        }
        composable(route = RouteScreen.AnswerAudioScreen.route) {
            AnswerAudioScreen()
        }
        composable(route = RouteScreen.AnswerTextScreen.route) {
//            AnswerTextScreen()
        }
    }
}