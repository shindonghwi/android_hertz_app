package mago.apps.hertz.ui.screens.answer.text.frequency_exist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.input.CustomTextField
import mago.apps.hertz.ui.components.input.KeyBoardActionUnit
import mago.apps.hertz.ui.model.emotion.EmotionType
import mago.apps.hertz.ui.screens.answer.text.AnswerTextViewModel
import mago.apps.hertz.ui.screens.answer.text.common.QuestionContent
import mago.apps.hertz.ui.screens.answer.text.common.TagInfo
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FrequencyNotExistAnswerContent(modifier: Modifier, answerTextViewModel: AnswerTextViewModel) {
    Column(modifier = modifier) {
        QuestionContent()

        // 날짜 & 좋아요 영역
        DayAndLikeContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 10.dp),
        )

        // 답변 영역
        InputAnswer(
            modifier = Modifier
                .padding(top = 13.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
                .padding(14.dp)
        )

        // 감정 주파수 선택
        TodayFrequencyTitle(
            modifier = Modifier.padding(
                top = 30.dp, start = 20.dp, end = 20.dp
            )
        )

        // 태그 정보
        TagInfo(
            modifier = Modifier.padding(top = 12.dp, start = 20.dp, end = 20.dp),
            answerTextViewModel = answerTextViewModel
        )
    }
}

@Composable
private fun DayAndLikeContent(modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = SimpleDateFormat(
                "yyyy년 MM월 dd일 EE요일", Locale.getDefault()
            ).format(Calendar.getInstance().time),
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
            style = MaterialTheme.typography.titleMedium
        )
        Icon(
            modifier = Modifier
                .size(46.dp)
                .noDuplicationClickable {}
                .padding(8.dp),
            imageVector = Icons.Filled.FavoriteBorder,
            tint = MaterialTheme.colorScheme.error,
            contentDescription = null,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun InputAnswer(modifier: Modifier) {
    val keyboardController = LocalSoftwareKeyboardController.current

    CustomTextField(
        modifier = modifier,
        textStyle = MaterialTheme.typography.bodyLarge,
        placeholderText = {
            Text(
                text = stringResource(id = R.string.answer_text_placeholder),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                textAlign = TextAlign.Center
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyBoardActionUnit = KeyBoardActionUnit(
            onDone = { keyboardController?.hide() }
        )
    )
}


@Composable
private fun TodayFrequencyTitle(modifier: Modifier) {
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
                .wrapContentHeight()
        )
    }
}

@Composable
private fun EmotionPercentSelectView(modifier: Modifier) {
    val selectedValue = remember { mutableStateOf(EmotionType.HAPPINESS) }
    val emotionList = listOf(
        Pair("\uD83D\uDE04", EmotionType.HAPPINESS),
        Pair("\uD83D\uDE22", EmotionType.SADNESS),
        Pair("\uD83D\uDE21", EmotionType.ANGER),
        Pair("\uD83D\uDE36", EmotionType.NEUTRAL),
    )

    Row(
        modifier = modifier, horizontalArrangement = Arrangement.SpaceAround
    ) {
        repeat(emotionList.size) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = emotionList[it].first,
                    style = MaterialTheme.typography.titleMedium,
                )
                RadioButton(selected = selectedValue.value == emotionList[it].second,
                    onClick = { selectedValue.value = emotionList[it].second })
            }
        }
    }
}