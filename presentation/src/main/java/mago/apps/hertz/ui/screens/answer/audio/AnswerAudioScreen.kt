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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import mago.apps.domain.model.question.QuestionRandom
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.animation.WavesAnimation
import mago.apps.hertz.ui.components.dialog.CustomPopup
import mago.apps.hertz.ui.components.dialog.PopupType
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import mago.apps.hertz.ui.utils.recorder.FileMultipart
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnMain


@Composable
fun AnswerAudioScreen(
    answerAudioViewModel: AnswerAudioViewModel, question: QuestionRandom
) {
    answerAudioViewModel.run {
        updateQuestionInfo(question)
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

    Column(modifier = Modifier.fillMaxSize()) {
        QuestionContent(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
                .background(MaterialTheme.colorScheme.onPrimary),
            answerAudioViewModel = answerAudioViewModel
        )
        AudioRecordingContent(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
                .background(MaterialTheme.colorScheme.primary),
            answerAudioViewModel = answerAudioViewModel,
        )
    }

    PostAnswerVoicePopup(answerAudioViewModel)
    ErrorAnswerVoicePopup(answerAudioViewModel)

}

@Composable
fun ErrorAnswerVoicePopup(answerAudioViewModel: AnswerAudioViewModel) {
    val answerVoiceState = answerAudioViewModel.postAnswerVoiceState.collectAsState().value
    CustomPopup(
        isVisible = answerVoiceState.isErrorState,
        backgroundTouchEnable = true,
        type = PopupType.FALLBACK,
        fallbackMessage = answerVoiceState.error
    )
}

@Composable
fun PostAnswerVoicePopup(answerAudioViewModel: AnswerAudioViewModel) {
    val answerVoiceState = answerAudioViewModel.postAnswerVoiceState.collectAsState().value
    CustomPopup(
        isVisible = answerVoiceState.isLoading,
        backgroundTouchEnable = false,
        type = PopupType.RECORD_END_FREQUENCY,
    )
}


@Composable
private fun QuestionContent(modifier: Modifier, answerAudioViewModel: AnswerAudioViewModel) {
    Column(
        modifier = modifier.verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = answerAudioViewModel.questionInfo?.text.toString(),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight(800)),
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(top = 30.dp),
            text = answerAudioViewModel.questionInfo?.example.toString(),
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
    val context = LocalContext.current
    val currentTime = answerAudioViewModel.pcmRecorder.getCurrentTime().collectAsState().value

    Text(
        modifier = Modifier.padding(top = 20.dp, bottom = 60.dp),
        text = "$currentTime / 5:00",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
    )

    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(key1 = Unit) {

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    answerAudioViewModel.pcmRecorder.run {
                        createRecorder(context = context)
                        start()
                    }
                }
                Lifecycle.Event.ON_PAUSE -> {
                    answerAudioViewModel.pcmRecorder.run {
                        stop()
                    }
                }
                else -> {}
            }
        }
        val lifecycle = lifecycleOwner.value.lifecycle
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

}

@Composable
private fun PlayingContent(answerAudioViewModel: AnswerAudioViewModel) {

    WavesAnimation(
        waveSize = 80.dp,
        waveColor = Color.White.copy(alpha = 0.4f),
    ) {
        Icon(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary)
                .noDuplicationClickable {
                    answerAudioViewModel.run {
                        pcmRecorder.stop()
                        coroutineScopeOnMain {
                            val body = FileMultipart.getFileBody("file", pcmRecorder.getZipFile())
                            postAnswerVoice(3, body)
                        }
                    }
                }
                .padding(25.dp),
            painter = painterResource(id = R.drawable.stop),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
    }

    /** 녹음중이고, 시간이 흘러간경우에만 "녹음완료" 버튼을 보여준다. */
    Text(
        modifier = Modifier
            .padding(top = 80.dp)
            .wrapContentWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(12.dp))
            .noDuplicationClickable {
                answerAudioViewModel.run {
                    pcmRecorder.stop()
                    coroutineScopeOnMain {
                        val body = FileMultipart.getFileBody("file", pcmRecorder.getZipFile())
                        postAnswerVoice(3, body)
                    }
                }
            }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        text = stringResource(id = R.string.home_bottombar_answer_audio_stop),
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold
        ),
        textAlign = TextAlign.Center,
    )
}
