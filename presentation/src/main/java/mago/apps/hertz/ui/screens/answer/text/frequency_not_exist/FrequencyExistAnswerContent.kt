package mago.apps.hertz.ui.screens.answer.text.frequency_not_exist

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FrequencyExistAnswerContent(modifier: Modifier) {

}


@Composable
private fun TodayFrequencyTitle(modifier: Modifier) {
    Column(
        modifier = modifier,
    ) {
//        Text(
//            text = stringResource(id = R.string.home_bottombar_answer_today_emotion_title),
//            color = MaterialTheme.colorScheme.primary,
//            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
//        )

        EmotionPercentView(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}

@Composable
private fun EmotionPercentView(modifier: Modifier) {

    val emotionList = listOf(
        Pair("\uD83D\uDE04", 85),
        Pair("\uD83D\uDE22", 20),
        Pair("\uD83D\uDE21", 5),
        Pair("\uD83D\uDE36", 37),
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
                Text(
                    text = " ${emotionList[it].second}%",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}
