package mago.apps.hertz.ui.screens.answer.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.input.CustomTextField
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AnswerTextScreen(){
    AnswerTextContent(modifier = Modifier.fillMaxSize())
}

@Composable
private fun AnswerTextContent(
    modifier: Modifier
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Text(
            text = SimpleDateFormat(
                "yyyy년 MM월 dd일 EE요일", Locale.getDefault()
            ).format(Calendar.getInstance().time),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            style = MaterialTheme.typography.titleMedium
        )

        CustomTextField(
            modifier = Modifier
                .padding(top = 13.dp)
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
                .padding(14.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
        )

        Row(
            modifier = Modifier
                .padding(top = 45.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "00:23",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = "주파수자리",
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                textAlign = TextAlign.Center
            )

            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.play),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.home_bottombar_answer_today_emotion_title),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )


            EmotionPercentView(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }

        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.home_bottombar_answer_today_tag),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            /** TODO: 태그 자리 들어와야함 */
            Text(
                text = "여기에 태그자리", color = Color.Black, style = MaterialTheme.typography.titleMedium
            )

        }
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
