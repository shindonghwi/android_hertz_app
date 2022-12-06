package mago.apps.hertz.ui.screens.question

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.input.CustomTextField
import mago.apps.hertz.ui.utils.compose.animation.WavesAnimation
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun QuestionScreen(navController: NavHostController) {
    QuestionContent(modifier = Modifier.fillMaxSize())
}


@Composable
private fun QuestionAppBarTitleContent() {
    Text(
        modifier = Modifier.padding(start = 8.dp),
        text = stringResource(id = R.string.home_title_2),
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun QuestionAppBarLeftContent() {
    Icon(
        modifier = Modifier.size(44.dp),
        painter = painterResource(id = R.drawable.profile_sample),
        contentDescription = null,
        tint = Color.Unspecified
    )
}

@Composable
fun QuestionAppBarRightContent() {

    val rightIcons: List<Pair<ImageVector, () -> Unit>> = listOf(
        Pair(Icons.Default.Home, {}),
        Pair(Icons.Default.Menu, {}),
        Pair(Icons.Default.Notifications, {}),
    )

    rightIcons.forEach {
        Icon(modifier = Modifier
            .size(40.dp)
            .noDuplicationClickable {
                it.second()
            }
            .padding(6.dp),
            imageVector = it.first,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outlineVariant)
    }
}


// 텍스트로 답하기(확장)
@Composable
private fun QuestionBottomBarTextAnswerExpanded(
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


// 음성으로 답하기(확장)
@Composable
private fun QuestionBottomBarAudioAnswerExpanded(modifier: Modifier) {

    val isPlaying = remember { mutableStateOf(false) }

    Box(
        modifier = modifier,
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(7.dp),
            progress = 0.5f,
            color = Color.White.copy(alpha = 0.5f),
            trackColor = MaterialTheme.colorScheme.primary
        )


        Column(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = "00:09",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )

            Box(
                modifier = Modifier.weight(1f), contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .noDuplicationClickable {
                            isPlaying.value = !isPlaying.value
                        }, horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isPlaying.value) {
                        WavesAnimation(
                            waveSize = 80.dp, waveColor = Color.White.copy(alpha = 0.3f)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.onPrimary)
                                    .padding(0.dp),
                                painter = painterResource(id = R.drawable.pause_circle),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        Icon(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onPrimary)
                                .padding(if (isPlaying.value) 0.dp else 16.dp),
                            painter = painterResource(id = R.drawable.align_right),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }


                    Text(
                        modifier = Modifier.padding(top = 14.dp),
                        text = if (isPlaying.value) {
                            stringResource(id = R.string.home_bottombar_answer_audio_stop)
                        } else {
                            stringResource(id = R.string.home_bottombar_answer_audio_play)
                        },
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestionContent(modifier: Modifier) {
    val isVisible = remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        // 질문 영역
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            QuestionText(isVisible.value)
        }

        // 셔플 아이콘 영역
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp), contentAlignment = Alignment.BottomEnd
        ) {
            Icon(modifier = Modifier
                .size(36.dp)
                .noDuplicationClickable {
                    isVisible.value = !isVisible.value
                }
                .padding(6.dp),
                painter = painterResource(id = R.drawable.random),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuestionText(isVisible: Boolean) {

    val textList = listOf<String>(
        "내가 어른이 됐다고\n느낄 때는?",
        "당신이 가장 좋아했던\n사람은 누구인가요?",
        "지금 있는 곳에서\n누군가에게\n들려주고픈 노래는?",
        "인생이란 무엇인가?",
        "퇴근할때 듣고 싶은\n노래는 무엇인가요?",
        "맛있게 먹는 야식은\n살찐다 VS 안찐다",
    )
    val max_num_value = textList.size - 1
    val min_num_value = 0
    val randomText = textList[Random().nextInt(max_num_value - min_num_value + 1) + min_num_value]

    /** TODO: 질문 텍스트 동적 변경 필요 */
    AnimatedContent(
        targetState = isVisible,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(300)
            ) with fadeOut(
                animationSpec = tween(300)
            )
        },
    ) { targetState ->
        Text(
            modifier = Modifier.verticalScroll(state = rememberScrollState()),
            text = randomText,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight(
                    800
                )
            ),
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
    }
}
