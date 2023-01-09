package mago.apps.hertz.ui.screens.answer.register.audio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
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
import androidx.navigation.NavHostController
import com.google.gson.Gson
import mago.apps.domain.model.question.QuestionRandom
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.animation.WavesAnimation
import mago.apps.hertz.ui.components.appbar.AppBarContent
import mago.apps.hertz.ui.components.dialog.CustomPopup
import mago.apps.hertz.ui.components.dialog.IBackPressEvent
import mago.apps.hertz.ui.components.dialog.PopupType
import mago.apps.hertz.ui.model.screen.RouteScreen
import mago.apps.hertz.ui.navigation.navigateWithPopUp
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import mago.apps.hertz.ui.utils.recorder.FileMultipart
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnDefault
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnMain


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerAudioScreen(
    navController: NavHostController,
    answerAudioViewModel: AnswerAudioViewModel,
    question: QuestionRandom
) {
    answerAudioViewModel.run {
        updateQuestionInfo(question)
        AnswerAudioLifecycle(this)
    }

    Scaffold(topBar = { AnswerAudioAppBar(navController) }) {
        AnswerAudioContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            answerAudioViewModel = answerAudioViewModel,
            navController = navController
        )
    }
}

@Composable
private fun AnswerAudioAppBar(navController: NavHostController) {
    AppBarContent(
        leftContent = {
            Icon(modifier = Modifier
                .size(40.dp)
                .noDuplicationClickable {
                    navController.popBackStack()
                }
                .padding(6.dp),
                imageVector = Icons.Default.ArrowBack,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null
            )
        },
        centerContent = {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = R.string.answer_audio_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    )
}

@Composable
private fun AnswerAudioLifecycle(answerAudioViewModel: AnswerAudioViewModel) {
    val context = LocalContext.current
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
private fun AnswerAudioContent(
    modifier: Modifier,
    answerAudioViewModel: AnswerAudioViewModel,
    navController: NavHostController
) {

    Column(modifier = modifier) {
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
    ResultAnswerVoicePopup(answerAudioViewModel, navController)

}

@Composable
fun ResultAnswerVoicePopup(
    answerAudioViewModel: AnswerAudioViewModel,
    navController: NavHostController
) {
    val answerVoiceState = answerAudioViewModel.postAnswerVoiceState.collectAsState().value

    if (answerVoiceState.isErrorState.value) {
        CustomPopup(
            isVisible = answerVoiceState.isErrorState,
            backgroundTouchEnable = true,
            type = PopupType.FALLBACK,
            showingMessage = answerVoiceState.error,
            iBackPressEvent = object : IBackPressEvent {
                override fun onPress() {
                    navController.popBackStack()
                }
            }
        )
    }

    LaunchedEffect(key1 = answerVoiceState, block = {
        answerVoiceState.data?.let {
            answerAudioViewModel.run {
                coroutineScopeOnDefault {
                    updateVoiceRegisterState(VoiceRegisterState())
                }
            }
            navController.navigateWithPopUp(
                route = RouteScreen.AnswerDetailScreen.route
                        + "?answer=${Gson().toJson(it)}",
                popUpRoute = RouteScreen.QuestionScreen
            )
        }
    })
}

@Composable
private fun PostAnswerVoicePopup(answerAudioViewModel: AnswerAudioViewModel) {
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
            modifier = Modifier.padding(horizontal = 20.dp),
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
        LinearProgressBar(answerAudioViewModel)

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
private fun LinearProgressBar(answerAudioViewModel: AnswerAudioViewModel) {

    val currentTimeInfo = answerAudioViewModel.pcmRecorder.getCurrentTime().collectAsState().value
    val timeToInt = currentTimeInfo.first

    LaunchedEffect(key1 = timeToInt, block = {
        if (timeToInt == answerAudioViewModel.pcmRecorder.getMaxTime()) {
            requestRecordEnd(answerAudioViewModel)
        }
    })

    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .height(7.dp),
        progress = if (timeToInt == 0) {
            0f
        } else {
            timeToInt / answerAudioViewModel.pcmRecorder.getMaxTime().toFloat()
        },
        color = Color.White.copy(alpha = 0.5f),
        trackColor = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun PlayTimeContent(answerAudioViewModel: AnswerAudioViewModel) {
    val currentTime = answerAudioViewModel.pcmRecorder.getCurrentTime().collectAsState().value
    val timeText = currentTime.second
    val maxTime = answerAudioViewModel.pcmRecorder.getMaxTime()

    Text(
        modifier = Modifier.padding(top = 20.dp, bottom = 60.dp),
        text = "$timeText / ${answerAudioViewModel.pcmRecorder.convertTimeToString(maxTime)}",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
    )
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
                    requestRecordEnd(answerAudioViewModel)
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
                requestRecordEnd(answerAudioViewModel)
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

fun requestRecordEnd(answerAudioViewModel: AnswerAudioViewModel) {
    answerAudioViewModel.run {
        pcmRecorder.stop()
        coroutineScopeOnMain {
            val body = FileMultipart.getFileBody("file", pcmRecorder.getZipFile())
            answerAudioViewModel.questionInfo?.questionSeq?.let { seq ->
                postAnswerVoice(seq, body)
            }
        }
    }
}
