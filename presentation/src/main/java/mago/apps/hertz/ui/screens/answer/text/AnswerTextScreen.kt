package mago.apps.hertz.ui.screens.answer.text

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.question.QuestionRandom
import mago.apps.hertz.ui.screens.answer.text.frequency_exist.FrequencyNotExistAnswerContent
import mago.apps.hertz.ui.screens.answer.text.frequency_not_exist.FrequencyExistAnswerContent

@Composable
fun AnswerTextScreen(
    navController: NavHostController,
    answerTextViewModel: AnswerTextViewModel,
    question: QuestionRandom,
    answer: Answer?
) {
    answerTextViewModel.run {
        updateQuestionInfo(question)
        screenScrollState = rememberScrollState()
    }

    AnswerTextContent(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(answerTextViewModel.screenScrollState),
        answerTextViewModel = answerTextViewModel
    )
}

@Composable
private fun AnswerTextContent(
    modifier: Modifier,
    answerTextViewModel: AnswerTextViewModel
) {
//    val isFrequencyExist = answerTextViewModel.isFrequencyExistState.value
//
//    if (isFrequencyExist) {
//        FrequencyExistAnswerContent(modifier)
//    } else {
//        FrequencyNotExistAnswerContent(modifier, answerTextViewModel)
//    }

}
