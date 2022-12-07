package mago.apps.hertz.ui.screens.answer.audio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.dialog.CustomPopup
import mago.apps.hertz.ui.components.dialog.PopupType
import mago.apps.hertz.ui.utils.compose.animation.WavesAnimation
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnDefault

@Composable
fun AnswerAudioScreen(answerAudioViewModel: AnswerAudioViewModel) {
    answerAudioViewModel.run {
        AnswerAudioContent(this)
        AnswerAudioLifecycle(this)
    }
}

@Composable
private fun AnswerAudioLifecycle(answerAudioViewModel: AnswerAudioViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                }
                Lifecycle.Event.ON_PAUSE -> {
                    coroutineScopeOnDefault {
                        answerAudioViewModel.updatePlayingState(false)
                    }
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun AnswerAudioContent(answerAudioViewModel: AnswerAudioViewModel) {
    val isFrequencyPopUpVisible = remember { answerAudioViewModel.isFrequencyPopUpVisible }

    Column(modifier = Modifier.fillMaxSize()) {
        QuestionContent(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
                .background(MaterialTheme.colorScheme.onPrimary)
        )
        AudioRecordingContent(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
                .background(MaterialTheme.colorScheme.primary),
            answerAudioViewModel = answerAudioViewModel,
        )
    }

    CustomPopup(
        isVisible = isFrequencyPopUpVisible,
        type = PopupType.RECORD_END_FREQUENCY,
    )
}


@Composable
private fun QuestionContent(modifier: Modifier) {
    Column(
        modifier = modifier.verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "내가 어른이 됐다고\n느낄 때는?",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight(800)),
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(top = 30.dp),
            text = "예: 2만원짜리 스파게티 먹을 때 왜냐하면,,,",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight(700)),
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AudioRecordingContent(
    modifier: Modifier,
    answerAudioViewModel: AnswerAudioViewModel,
) {
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
            answerAudioViewModel.run {
                PlayTimeContent(this)
                PlayingContent(this)
            }
        }
    }
}

@Composable
private fun PlayTimeContent(answerAudioViewModel: AnswerAudioViewModel) {
    Text(
        modifier = Modifier.padding(top = 20.dp, bottom = 60.dp),
        text = "00:09 / 10:00",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
    )
}

@Composable
private fun PlayingContent(answerAudioViewModel: AnswerAudioViewModel) {

    val isPlaying = remember { answerAudioViewModel.isPlaying }

    WavesAnimation(
        waveSize = 80.dp,
        waveColor = Color.White.copy(alpha = if (isPlaying.value) 0.4f else 0.05f),
    ) {
        Icon(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary)
                .noDuplicationClickable {
                    coroutineScopeOnDefault {
                        answerAudioViewModel.updatePlayingState(!isPlaying.value)
                    }
                }
                .padding(5.dp),
            painter = painterResource(id = if (isPlaying.value) R.drawable.pause else R.drawable.play),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }

    /** 녹음중이고, 시간이 흘러간경우에만 "녹음완료" 버튼을 보여준다. */
    if (isPlaying.value) {
        Text(
            modifier = Modifier
                .padding(top = 80.dp)
                .wrapContentWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(12.dp))
                .noDuplicationClickable {
                    answerAudioViewModel.run {
                        coroutineScopeOnDefault {
                            updatePlayingState(false)
                            updatePopupState(true)
                        }
                    }
                }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            text = stringResource(id = R.string.home_bottombar_answer_audio_stop),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
    }
}
