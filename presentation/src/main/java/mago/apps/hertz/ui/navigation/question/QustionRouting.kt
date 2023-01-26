package mago.apps.hertz.ui.navigation.question

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import mago.apps.hertz.ui.model.screen.RouteScreen
import mago.apps.hertz.ui.screens.question.QuestionScreen
import mago.apps.hertz.ui.screens.question.QuestionViewModel

fun NavGraphBuilder.questionRouting(navController: NavHostController) {

    /** @feature: 질문 화면
     * @author: 2023/01/09 3:44 PM donghwishin
     */
    composable(
        route = RouteScreen.QuestionScreen.route
                + "?questionSeq={questionSeq}",
        arguments = listOf(
            navArgument("questionSeq") {
                type = NavType.StringType
                nullable = true
            },
        ),
    ) {
        val questionViewModel = hiltViewModel<QuestionViewModel>()
        val questionSeq = it.arguments?.getString("questionSeq")
        QuestionScreen(
            navController,
            questionViewModel,
            questionSeq?.toIntOrNull()
        )
    }
}