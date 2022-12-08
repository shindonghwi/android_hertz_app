package mago.apps.hertz.ui.screens.answer.text

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import mago.apps.hertz.ui.screens.answer.text.frequency_exist.FrequencyNotExistAnswerContent
import mago.apps.hertz.ui.screens.answer.text.frequency_not_exist.FrequencyExistAnswerContent

@Composable
fun AnswerTextScreen(answerTextViewModel: AnswerTextViewModel) {
    answerTextViewModel.screenScrollState = rememberScrollState()

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
    val isFrequencyExist = answerTextViewModel.isFrequencyExistState.value

    if (isFrequencyExist) {
        FrequencyExistAnswerContent(modifier)
    } else {
        FrequencyNotExistAnswerContent(modifier, answerTextViewModel)
    }

}
