package mago.apps.hertz.ui.screens.answer.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import mago.apps.domain.model.common.EmotionList
import mago.apps.domain.model.common.EmotionType
import mago.apps.hertz.R
import mago.apps.hertz.ui.screens.answer.detail.AnswerDetailViewModel
import mago.apps.hertz.ui.screens.answer.edit.AnswerEditViewModel
import mago.apps.hertz.ui.screens.answer.register.text.AnswerTextViewModel

@Composable
fun <T> TodayFrequencySelector(modifier: Modifier, vm: T) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(id = R.string.answer_text_title_today_emotion_frequency),
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        EmotionPercentSelectView(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            vm = when (vm) {
                is AnswerTextViewModel -> vm
                is AnswerEditViewModel -> vm
                else -> null
            }
        )
    }
}

@Composable
private fun <T> EmotionPercentSelectView(modifier: Modifier, vm: T) {
    val selectedValue = remember { mutableStateOf(EmotionType.HAPPINESS) }

    Row(
        modifier = modifier, horizontalArrangement = Arrangement.SpaceAround
    ) {
        repeat(EmotionList.size) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = EmotionList[it].first,
                    style = MaterialTheme.typography.titleMedium,
                )
                RadioButton(
                    selected = selectedValue.value == EmotionList[it].second,
                    onClick = {
                        when (vm) {
                            is AnswerTextViewModel -> {
                                vm.updateEmotion(EmotionList[it].second)
                            }
                            is AnswerEditViewModel -> {
                                vm.updateEmotion(EmotionList[it].second)
                            }
                            else -> {}
                        }
                        selectedValue.value = EmotionList[it].second
                    },
                )
            }
        }
    }
}