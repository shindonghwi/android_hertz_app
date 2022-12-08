package mago.apps.hertz.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import mago.apps.hertz.ui.model.screen.RouteScreen
import mago.apps.hertz.ui.screens.answer.audio.AnswerAudioScreen
import mago.apps.hertz.ui.screens.answer.audio.AnswerAudioViewModel
import mago.apps.hertz.ui.screens.answer.text.AnswerTextScreen
import mago.apps.hertz.ui.screens.answer.text.AnswerTextViewModel
import mago.apps.hertz.ui.screens.category.CategoryScreen
import mago.apps.hertz.ui.screens.episode_save.EpisodeSaveScreen
import mago.apps.hertz.ui.screens.home.HomeScreen
import mago.apps.hertz.ui.screens.notification.NotificationScreenScreen
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
            val answerAudioViewModel = hiltViewModel<AnswerAudioViewModel>()
            AnswerAudioScreen(answerAudioViewModel)
        }
        composable(route = RouteScreen.AnswerTextScreen.route) {
            val answerTextViewModel = hiltViewModel<AnswerTextViewModel>()
            AnswerTextScreen(answerTextViewModel)
        }
        composable(route = RouteScreen.NotificationScreen.route) {
            NotificationScreenScreen()
        }
        composable(route = RouteScreen.EpisodeSaveScreen.route) {
            EpisodeSaveScreen()
        }
    }
}