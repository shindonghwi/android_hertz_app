package mago.apps.hertz.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.question.QuestionRandom
import mago.apps.hertz.ui.model.screen.RouteScreen
import mago.apps.hertz.ui.screens.answer.detail.AnswerDetailScreen
import mago.apps.hertz.ui.screens.answer.detail.AnswerDetailViewModel
import mago.apps.hertz.ui.screens.answer.edit.AnswerEditScreen
import mago.apps.hertz.ui.screens.answer.edit.AnswerEditViewModel
import mago.apps.hertz.ui.screens.answer.register.audio.AnswerAudioScreen
import mago.apps.hertz.ui.screens.answer.register.audio.AnswerAudioViewModel
import mago.apps.hertz.ui.screens.answer.register.text.AnswerTextScreen
import mago.apps.hertz.ui.screens.answer.register.text.AnswerTextViewModel
import mago.apps.hertz.ui.screens.episode_list.EpisodeListScreen
import mago.apps.hertz.ui.screens.episode_list.EpisodeListViewModel
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
        composable(route = RouteScreen.EpisodeListScreen.route) {
            val episodeListViewModel = hiltViewModel<EpisodeListViewModel>()
            EpisodeListScreen(navController, episodeListViewModel)
        }
        composable(
            route = RouteScreen.AnswerAudioScreen.route
                    + "?question={question}",
            arguments = listOf(
                navArgument("question") {
                    type = NavType.StringType
                },
            ),
        ) {
            val question = it.arguments?.getString("question")
            val answerAudioViewModel = hiltViewModel<AnswerAudioViewModel>()
            AnswerAudioScreen(
                navController,
                answerAudioViewModel,
                Gson().fromJson(question, QuestionRandom::class.java)
            )
        }
        composable(
            route = RouteScreen.AnswerTextScreen.route
                    + "?question={question}",
            arguments = listOf(
                navArgument("question") {
                    type = NavType.StringType
                },
            ),
        ) {
            val question = it.arguments?.getString("question")
            val answerTextViewModel = hiltViewModel<AnswerTextViewModel>()
            AnswerTextScreen(
                navController,
                answerTextViewModel,
                Gson().fromJson(question, QuestionRandom::class.java),
            )
        }
        composable(
            route = RouteScreen.AnswerDetailScreen.route
                    + "?answer={answer}"
                    + "&answerSeq={answerSeq}",
            arguments = listOf(
                navArgument("answer") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("answerSeq") {
                    type = NavType.StringType
                    nullable = true
                },
            ),
        ) {
            val answer = it.arguments?.getString("answer")
            val answerSeq = it.arguments?.getString("answerSeq")
            val answerDetailViewModel = hiltViewModel<AnswerDetailViewModel>()
            AnswerDetailScreen(
                navController,
                answerDetailViewModel,
                answerSeq,
                Gson().fromJson(answer, Answer::class.java)
            )
        }
        composable(
            route = RouteScreen.AnswerEditScreen.route
                    + "?answer={answer}",
            arguments = listOf(
                navArgument("answer") {
                    type = NavType.StringType
                    nullable = true
                }
            ),
        ) {
            val answer = it.arguments?.getString("answer")
            val answerEditViewModel = hiltViewModel<AnswerEditViewModel>()
            AnswerEditScreen(
                navController,
                answerEditViewModel,
                Gson().fromJson(answer, Answer::class.java)
            )
        }

        composable(route = RouteScreen.NotificationScreen.route) {
            NotificationScreenScreen(navController)
        }
        composable(route = RouteScreen.EpisodeSaveScreen.route) {
            EpisodeSaveScreen()
        }
    }
}