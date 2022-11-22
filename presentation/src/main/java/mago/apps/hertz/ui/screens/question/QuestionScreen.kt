package mago.apps.hertz.ui.screens.question

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import mago.apps.hertz.R
import mago.apps.hertz.ui.screens.components.appbar.AppBar
import mago.apps.hertz.ui.screens.components.appbar.AppbarType
import mago.apps.hertz.ui.utils.compose.animation.WavesAnimation
import mago.apps.hertz.ui.utils.compose.modifier.backgroundVGradient
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable

enum class AnswerState { IDLE, TEXT, AUDIO }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(navController: NavHostController) {
    Scaffold(topBar = {
        AppBar(type = AppbarType.ICON_TITLE_ICON,
            textContent = { QuestionAppBarTitleContent() },
            leftContent = { QuestionAppBarLeftContent() },
//            rightContent = { QuestionAppBarRightContent() }
        )
    }, bottomBar = {
        QuestionBottomBar()
    }) {
        Box(modifier = Modifier.fillMaxSize()) {

            // background Image
            /** TODO: 백그라운드 gradient 이미지 적용해야함. */
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .backgroundVGradient(listOf(Color.White, Color(0x33162EFF)))
            ) {}
//            Image(
//                modifier = Modifier.fillMaxSize(),
//                painter = painterResource(id = R.drawable.bg),
//                contentDescription = "backgroundImage",
//                contentScale = ContentScale.Crop
//            )

            // main content
            QuestionContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QuestionBottomBar() {
    val answerState = remember { mutableStateOf(AnswerState.IDLE) }
    val isAnswerMode = answerState.value != AnswerState.IDLE

    // 바텀바 높이
    val configuration = LocalConfiguration.current
    val bottomHeight = configuration.screenHeightDp.dp * 0.4f
    val textAnswerHeight =
        configuration.screenHeightDp.dp * 0.7f // 텍스트로 답하기 클릭했을때 나오는 UI
    val audioAnswerHeight =
        configuration.screenHeightDp.dp * 0.55f // 음성으로 답하기 클릭했을때 나오는 UI

    AnimatedVisibility(
        visible = !isAnswerMode,
        enter = fadeIn(animationSpec = tween(durationMillis = 300)) + expandIn(),
        exit = shrinkOut() + fadeOut(animationSpec = tween(durationMillis = 300)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(bottomHeight),
        ) {
            QuestionBottomBarTextAnswer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background)
                    .noDuplicationClickable {
                        answerState.value = AnswerState.TEXT
                    }, isExpanded = false
            )
            QuestionBottomBarAudioAnswer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.primary)
                    .noDuplicationClickable {
                        answerState.value = AnswerState.AUDIO
                    },
            )
        }
    }
    AnimatedVisibility(
        visible = isAnswerMode,
        enter = fadeIn(animationSpec = tween(durationMillis = 300)) + expandIn(),
        exit = shrinkOut() + fadeOut(animationSpec = tween(durationMillis = 300)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (answerState.value == AnswerState.TEXT) textAnswerHeight else audioAnswerHeight),
        ) {
            if (answerState.value == AnswerState.TEXT) {
                QuestionBottomBarTextAnswer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.background),
                    isExpanded = true
                )
            }

            if (answerState.value == AnswerState.AUDIO) {
                QuestionBottomBarAudioAnswerExpanded(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }

    /** TODO: 질문화면 백 프레스 이벤트 수정*/
    val ctx = LocalContext.current
    var count = remember { 0 }
    BackHandler(enabled = true) {
        if (isAnswerMode) {
            answerState.value = AnswerState.IDLE
        } else {
            count += 1
            Toast.makeText(ctx, "한번더 누르면 종료", Toast.LENGTH_SHORT).show()
            if (count == 2) {
                Toast.makeText(ctx, "앱 종료", Toast.LENGTH_SHORT).show()
                count = 0
            }
        }
    }
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
private fun QuestionAppBarRightContent() {
    Icon(modifier = Modifier
        .size(36.dp)
        .noDuplicationClickable {

        }
        .padding(4.dp),
        imageVector = Icons.TwoTone.Star,
        contentDescription = null)
    Icon(modifier = Modifier
        .padding(horizontal = 8.dp)
        .size(36.dp)
        .noDuplicationClickable {

        }
        .padding(4.dp),
        imageVector = Icons.TwoTone.Star,
        contentDescription = null)
    Icon(modifier = Modifier
        .size(36.dp)
        .noDuplicationClickable {

        }
        .padding(4.dp),
        imageVector = Icons.TwoTone.Star,
        contentDescription = null)
}


// 텍스트로 답하기
@Composable
private fun QuestionBottomBarTextAnswer(
    modifier: Modifier, isExpanded: Boolean
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 14.dp),
            text = stringResource(id = R.string.home_bottombar_answer_title_text),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            style = MaterialTheme.typography.titleMedium
        )

        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.home_bottombar_answer_hint_text),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = 0.25f
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.arrow_circle_up),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}


// 음성으로 답하기
@Composable
private fun QuestionBottomBarAudioAnswer(modifier: Modifier) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 14.dp),
            text = stringResource(id = R.string.home_bottombar_answer_title_audio),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium
        )

        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    painter = painterResource(id = R.drawable.align_right),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// 음성으로 답하기
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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = "00:09",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
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
                            waveSize = 80.dp,
                            waveColor = Color.White.copy(alpha = 0.3f)
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
    Box(modifier = modifier) {

        // 질문 영역
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 63.dp),
            contentAlignment = Alignment.Center
        ) {
            /** TODO: 질문 텍스트 동적 변경 필요 */
            Text(
                modifier = Modifier.verticalScroll(state = rememberScrollState()),
                text = "내가 어른이 됐다고\n 느낄 때는?",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight(
                        800
                    )
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        // 셔플 아이콘 영역
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Icon(modifier = Modifier
                .size(36.dp)
                .noDuplicationClickable {

                }
                .padding(6.dp),
                painter = painterResource(id = R.drawable.random),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null)
        }


    }
}