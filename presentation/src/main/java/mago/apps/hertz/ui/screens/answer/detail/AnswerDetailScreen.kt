package mago.apps.hertz.ui.screens.answer.detail

import android.media.AudioAttributes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.google.gson.Gson
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.answer.AnswerProperty
import mago.apps.domain.model.common.EmotionList
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.appbar.AppBarContent
import mago.apps.hertz.ui.components.dialog.CustomPopup
import mago.apps.hertz.ui.components.dialog.PopupType
import mago.apps.hertz.ui.components.input.CustomTextField
import mago.apps.hertz.ui.model.screen.RouteScreen
import mago.apps.hertz.ui.model.toast.TOAST_CODE_WAITING
import mago.apps.hertz.ui.navigation.navigateTo
import mago.apps.hertz.ui.screens.answer.common.DayAndLikeContent
import mago.apps.hertz.ui.screens.answer.common.ILikeActionCallback
import mago.apps.hertz.ui.screens.answer.common.QuestionContent
import mago.apps.hertz.ui.theme.light_sub_primary
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import mago.apps.hertz.ui.utils.compose.showToast
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerDetailScreen(
    navController: NavHostController,
    answerDetailViewModel: AnswerDetailViewModel,
    answerSeq: String?,
    answer: Answer?
) {
    SetAnswerData(answerDetailViewModel, answer, answerSeq)

    Scaffold(
        topBar = {
            AnswerDetailAppBar(navController, answerDetailViewModel)
        },
    ) {
        AnswerDetailContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState()),
            answerDetailViewModel = answerDetailViewModel
        )
    }

    AnswerDetailScreenLifecycle(answerDetailViewModel)
    BBiBBiPopUp(answerDetailViewModel)
}

@Composable
fun SetAnswerData(
    answerDetailViewModel: AnswerDetailViewModel,
    answer: Answer?,
    answerSeq: String?
) {
    val answerState = answerDetailViewModel.answerState.collectAsState().value

    answerDetailViewModel.run {
        LaunchedEffect(key1 = Unit, block = {
            answer?.let {
                answerDetailViewModel.run {
                    updateAnswerState(it)
                    updateAnswerMode(!it.voice?.voiceUrl.isNullOrEmpty())
                }
            } ?: run {
                if (!answerState.isSuccessState.value) {
                    answerSeq?.let {
                        answerDetailViewModel.getAnswerInfo(it.toInt())
                    }
                }
            }
        })
    }
}

@Composable
private fun AnswerDetailScreenLifecycle(answerDetailViewModel: AnswerDetailViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_DESTROY -> {
                    answerDetailViewModel.audioClear()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })
}

@Composable
private fun BBiBBiPopUp(answerDetailViewModel: AnswerDetailViewModel) {
    BBiBBiLoadingPopUp(answerDetailViewModel)
    BBiBBiErrorPopUp(answerDetailViewModel)
    BBiBBiSuccessPopUp(answerDetailViewModel)
}

@Composable
private fun BBiBBiLoadingPopUp(answerDetailViewModel: AnswerDetailViewModel) {
    val bbibbiState = answerDetailViewModel.bbibbiState.collectAsState().value
    if (bbibbiState.isLoading.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(36.dp))
        }
    }
}

@Composable
private fun BBiBBiErrorPopUp(answerDetailViewModel: AnswerDetailViewModel) {
    val bbibbiState = answerDetailViewModel.bbibbiState.collectAsState().value
    if (bbibbiState.isErrorState.value) {
        CustomPopup(
            isVisible = bbibbiState.isErrorState,
            type = PopupType.FALLBACK,
            showingMessage = bbibbiState.error
        )
    }
}

@Composable
private fun BBiBBiSuccessPopUp(answerDetailViewModel: AnswerDetailViewModel) {
    val bbibbiState = answerDetailViewModel.bbibbiState.collectAsState().value
    if (bbibbiState.isSuccessState.value) {
        CustomPopup(
            isVisible = bbibbiState.isSuccessState,
            type = PopupType.REGISTER,
            showingMessage = stringResource(id = R.string.dialog_bbibbi_send_success)
        )
    }
}

@Composable
private fun AnswerDetailAppBar(
    navController: NavHostController,
    answerDetailViewModel: AnswerDetailViewModel
) {
    val answerState = answerDetailViewModel.answerState.collectAsState().value
    val context = LocalContext.current

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
                contentDescription = null)
        },
        centerContent = {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = R.string.answer_detail_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary
            )
        },
        rightContent = {
            Icon(modifier = Modifier
                .size(36.dp)
                .noDuplicationClickable {
                    if (answerState.isSuccessState.value) {
                        navController.navigateTo(
                            RouteScreen.AnswerEditScreen.route +
                                    "?answer=${Gson().toJson(answerState.data)}"
                        )
                    } else {
                        context.showToast(TOAST_CODE_WAITING)
                    }

                }
                .padding(6.dp),
                imageVector = Icons.Outlined.Edit,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null)
        },
    )
}

@Composable
private fun AnswerDetailContent(
    modifier: Modifier, answerDetailViewModel: AnswerDetailViewModel
) {
    ErrorContent(answerDetailViewModel)
    DetailContent(modifier, answerDetailViewModel)
}

@Composable
private fun DetailContent(modifier: Modifier, answerDetailViewModel: AnswerDetailViewModel) {

    val answerState = answerDetailViewModel.answerState.collectAsState().value
    val visibleState = MutableTransitionState(answerState.isSuccessState.value)

    Column(modifier = modifier) {
        // 질문
        QuestionContent(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(light_sub_primary)
                .padding(14.dp),
            content = answerState.data?.question?.text,
            visibleState = visibleState
        )

        // 날짜 & 좋아요 영역
        DayAndLikeContent(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 20.dp, end = 20.dp),
            timeText = answerState.data?.createdAt,
            visibleState = visibleState,
            likeDefaultState = answerState.data?.question?.isLiked,
            iLikeActionCallback = object : ILikeActionCallback {
                override fun onState(likeState: Boolean) {
                    answerState.data?.let {
                        coroutineScopeOnDefault {
                            if (likeState) {
                                answerDetailViewModel.postLike(it.question.questionSeq)
                            } else {
                                answerDetailViewModel.delLike(it.question.questionSeq)
                            }
                        }
                    }
                }
            })

        // 녹음한 내용 정보
        AnswerAudioContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            duration = answerDetailViewModel.getTime(answerState.data?.voice?.duration?.toInt()),
            voiceUrl = answerState.data?.voice?.voiceUrl,
            waveformImageUrl = answerState.data?.voice?.waveformUrl,
            visibleState = visibleState,
            answerDetailViewModel = answerDetailViewModel
        )

        // 답변한 내용
        AnswerText(visibleState = visibleState, answerDetailViewModel = answerDetailViewModel)

        // 감정 주파수 %
        TodayFrequencyContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp, start = 20.dp, end = 20.dp),
            visibleState = visibleState,
            answerDetailViewModel = answerDetailViewModel
        )

        // 태그 정보
        TagInfoContainer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            visibleState = visibleState,
            answerDetailViewModel = answerDetailViewModel
        )

        // 삐삐전송하기, 우리의 감정주파수
        BBiBBiFrequencyContent(
            questionSeq = answerState.data?.question?.questionSeq,
            property = answerState.data?.property,
            visibleState = visibleState,
            answerDetailViewModel = answerDetailViewModel
        )
    }
}

@Composable
private fun AnswerAudioContent(
    modifier: Modifier,
    duration: String,
    voiceUrl: String?,
    waveformImageUrl: String?,
    visibleState: MutableTransitionState<Boolean> = MutableTransitionState(true),
    answerDetailViewModel: AnswerDetailViewModel,
) {
    if (!waveformImageUrl.isNullOrEmpty() && !voiceUrl.isNullOrEmpty()) {
        Box(modifier = modifier) {
            AnimatedVisibility(visibleState = visibleState) {
                AudioPlayLifecycle(voiceUrl, answerDetailViewModel)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = duration,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Image(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp),
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(waveformImageUrl)
                                .size(Size.ORIGINAL).crossfade(true).build()
                        ),
                        contentDescription = null,
                    )
                    AudioPlayIcon(answerDetailViewModel)
                }
            }
        }
    }

    LoadingBox(visibleState = visibleState, height = 50.dp)
}

@Composable
private fun AudioPlayLifecycle(audioUrl: String, answerDetailViewModel: AnswerDetailViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    if (answerDetailViewModel.mediaPlayer == null) {
                        answerDetailViewModel.run {
                            audioUrl
                            initPlayer()
                            mediaPlayer?.apply {
                                setAudioAttributes(
                                    AudioAttributes.Builder()
                                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
                                )
                                setOnCompletionListener {
                                    answerDetailViewModel.audioReset()
                                }
                                setDataSource(context, audioUrl.toUri())
                                prepare()
                            }
                        }
                    }
                }
                Lifecycle.Event.ON_PAUSE -> {
                    answerDetailViewModel.audioReset()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })
}

@Composable
private fun AudioPlayIcon(answerDetailViewModel: AnswerDetailViewModel) {

    val isPlaying = answerDetailViewModel.isPlaying.collectAsState().value

    Box(modifier = Modifier
        .size(34.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.primary)
        .noDuplicationClickable {
            answerDetailViewModel.run {
                if (isPlaying) {
                    audioReset()
                } else {
                    audioStart()
                }
            }

        }
        .padding(4.dp), contentAlignment = Alignment.Center) {
        Icon(
            modifier = Modifier.fillMaxSize(), painter = if (isPlaying) {
                painterResource(id = R.drawable.pause)
            } else {
                painterResource(id = R.drawable.play)
            }, contentDescription = "play", tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}


@Composable
private fun ErrorContent(answerDetailViewModel: AnswerDetailViewModel) {
    val answerState = answerDetailViewModel.answerState.collectAsState().value
    val visibleState = MutableTransitionState(answerState.isErrorState.value)
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visibleState = visibleState, enter = fadeIn(), exit = fadeOut()) {
//            Text(text = stringResource(id = R.string.answer_detail_error))
            Text(text = answerState.error)
        }
    }
}

@Composable
private fun BBiBBiFrequencyContent(
    questionSeq: Int?,
    property: AnswerProperty?,
    visibleState: MutableTransitionState<Boolean> = MutableTransitionState(true),
    answerDetailViewModel: AnswerDetailViewModel
) {
    val boxModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 14.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(MaterialTheme.colorScheme.primary)

    property?.takeIf { it.isConnected }?.apply {
        AnimatedVisibility(visibleState = visibleState) {
            FrequencyButton(boxModifier)
        }
    } ?: run {
        property?.takeIf { !it.isSent }?.apply {
            AnimatedVisibility(visibleState = visibleState) {
                BBiBBiButton(boxModifier, questionSeq, answerDetailViewModel)
            }
        }
    }
}

@Composable
private fun FrequencyButton(modifier: Modifier) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .then(Modifier.noDuplicationClickable {
                context.showToast("감정주파수 확인. 미구현")
            })
            .padding(horizontal = 12.dp, vertical = 8.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.answer_detail_connected_button),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
private fun BBiBBiButton(
    modifier: Modifier, questionSeq: Int?, answerDetailViewModel: AnswerDetailViewModel
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .then(Modifier.noDuplicationClickable {
                questionSeq?.let {
                    coroutineScopeOnDefault {
                        answerDetailViewModel.postSendQuestionFriend(it)
                    }
                } ?: run {
                    context.run { showToast(getString(R.string.toast_fail_bbibbi_send)) }
                }
            })
            .padding(horizontal = 12.dp, vertical = 8.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.answer_detail_bbibbi_button),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
private fun AnswerText(
    visibleState: MutableTransitionState<Boolean> = MutableTransitionState(true),
    answerDetailViewModel: AnswerDetailViewModel
) {
    val answerState = answerDetailViewModel.answerState.collectAsState().value

    Box(
        modifier = Modifier
            .padding(top = 13.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(9.dp))
            .background(light_sub_primary)
            .verticalScroll(rememberScrollState())
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visibleState = visibleState) {
            CustomTextField(
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.None),
                isSingleLine = false,
                defaultText = answerState.data?.voice?.text ?: "",
                enable = false,
            )
        }
    }
}

@Composable
private fun TodayFrequencyContent(
    modifier: Modifier,
    visibleState: MutableTransitionState<Boolean> = MutableTransitionState(true),
    answerDetailViewModel: AnswerDetailViewModel
) {
    if (answerDetailViewModel.isAudioAnswerMode) {
        TodayFrequencyAudio(modifier, answerDetailViewModel)
    } else {
        TodayFrequencyText(modifier, answerDetailViewModel)
    }
    LoadingBox(visibleState = visibleState, height = 40.dp)
}

@Composable
private fun TodayFrequencyAudio(
    modifier: Modifier,
    answerDetailViewModel: AnswerDetailViewModel
) {
    val answerState = answerDetailViewModel.answerState.collectAsState().value
    val emotionList = answerState.data?.voice?.emotionList

    if (!emotionList.isNullOrEmpty()) {
        Box(modifier = modifier) {
            Column {
                Text(
                    text = stringResource(id = R.string.answer_text_title_today_emotion_frequency),
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    repeat(emotionList.size) { idx ->

                        val type = emotionList[idx].type
                        val icon =
                            EmotionList.find { type == it.second.name }?.first.toString()
                        val iconType =
                            EmotionList.find { type == it.second.name }?.second.toString()
                        val rate = "${emotionList.find { it.type == iconType }?.rate}%"

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = icon,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                modifier = Modifier.padding(start = 4.dp),
                                text = rate,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TodayFrequencyText(
    modifier: Modifier,
    answerDetailViewModel: AnswerDetailViewModel
) {
    val answerState = answerDetailViewModel.answerState.collectAsState().value
    val emotion = answerState.data?.voice?.emotion

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(id = R.string.answer_text_title_today_emotion_frequency),
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = " ${EmotionList.find { it.second.name == emotion }?.first ?: ""}",
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun TagInfoContainer(
    modifier: Modifier,
    visibleState: MutableTransitionState<Boolean> = MutableTransitionState(true),
    answerDetailViewModel: AnswerDetailViewModel
) {
    val answerState = answerDetailViewModel.answerState.collectAsState().value
    val tagList = answerState.data?.tagList

    if (!tagList.isNullOrEmpty()) {
        Box(modifier = modifier) {
            AnimatedVisibility(visibleState = visibleState) {
                Column {
                    Text(
                        modifier = Modifier.padding(start = 20.dp),
                        text = stringResource(id = R.string.answer_text_title_tag),
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        state = rememberLazyListState()
                    ) {
                        itemsIndexed(items = tagList, key = { _, item -> item }) { _, item ->
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .wrapContentWidth()
                                    .height(30.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
                                    text = "#${item}",
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.labelMedium,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    LoadingBox(visibleState = visibleState, height = 40.dp)
}

@Composable
private fun LoadingBox(visibleState: MutableTransitionState<Boolean>, height: Dp) {
    if (!visibleState.targetState) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(height)
                .background(light_sub_primary)
        )
    }
}