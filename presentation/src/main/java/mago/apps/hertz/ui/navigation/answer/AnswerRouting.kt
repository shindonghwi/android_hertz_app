package mago.apps.hertz.ui.navigation.answer

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.question.QuestionRandom
import mago.apps.hertz.ui.model.screen.RouteScreen
import mago.apps.hertz.ui.screens.answer.connected.AnswerConnectedScreen
import mago.apps.hertz.ui.screens.answer.connected.AnswerConnectedViewModel
import mago.apps.hertz.ui.screens.answer.detail.AnswerDetailScreen
import mago.apps.hertz.ui.screens.answer.detail.AnswerDetailViewModel
import mago.apps.hertz.ui.screens.answer.edit.AnswerEditScreen
import mago.apps.hertz.ui.screens.answer.edit.AnswerEditViewModel
import mago.apps.hertz.ui.screens.answer.register.audio.AnswerAudioScreen
import mago.apps.hertz.ui.screens.answer.register.audio.AnswerAudioViewModel
import mago.apps.hertz.ui.screens.answer.register.text.AnswerTextScreen
import mago.apps.hertz.ui.screens.answer.register.text.AnswerTextViewModel

fun NavGraphBuilder.answerRouting(navController: NavHostController) {

    /** @feature: 음성 응답 화면
     * @author: 2023/01/09 3:44 PM donghwishin
     */
    composable(
        route = RouteScreen.AnswerAudioScreen.route + "?question={question}",
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

    /** @feature: 텍스트 응답 화면
     * @author: 2023/01/09 3:44 PM donghwishin
     */
    composable(
        route = RouteScreen.AnswerTextScreen.route + "?question={question}",
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

    /** @feature: 답변 상세 화면
     * @author: 2023/01/09 3:43 PM donghwishin
     */
    composable(
        route = RouteScreen.AnswerDetailScreen.route + "?answer={answer}" + "&answerSeq={answerSeq}",
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

    /** @feature: 편집 화면
     * @author: 2023/01/09 3:43 PM donghwishin
     */
    composable(
        route = RouteScreen.AnswerEditScreen.route + "?answer={answer}",
        arguments = listOf(navArgument("answer") {
            type = NavType.StringType
            nullable = true
        }),
    ) {
        val answer = it.arguments?.getString("answer")
        val answerEditViewModel = hiltViewModel<AnswerEditViewModel>()
        AnswerEditScreen(
            navController, answerEditViewModel, Gson().fromJson(answer, Answer::class.java)
        )
    }

    /** @feature: 우리의 감정 주파수 화면 (연결된 화면)
     * @author: 2023/01/09 4:01 PM donghwishin
     */
    composable(
        route = RouteScreen.AnswerConnectedScreen.route + "?answerSeq={answerSeq}",
        arguments = listOf(navArgument("answerSeq") {
            type = NavType.StringType
            nullable = true
        }),
    ) {
        val answerSeq = it.arguments?.getString("answerSeq")
        val answerConnectedViewModel = hiltViewModel<AnswerConnectedViewModel>()
        AnswerConnectedScreen(
            navController, answerConnectedViewModel, answerSeq
        )
    }
}