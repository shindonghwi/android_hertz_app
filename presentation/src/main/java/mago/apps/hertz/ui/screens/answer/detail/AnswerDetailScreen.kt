package mago.apps.hertz.ui.screens.answer.detail

import android.media.AudioAttributes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.answer.AnswerProperty
import mago.apps.domain.model.common.EmotionType
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.appbar.AppBarContent
import mago.apps.hertz.ui.components.dialog.CustomPopup
import mago.apps.hertz.ui.components.dialog.PopupType
import mago.apps.hertz.ui.components.input.CustomTextField
import mago.apps.hertz.ui.components.input.ITextCallback
import mago.apps.hertz.ui.screens.answer.common.DayAndLikeContent
import mago.apps.hertz.ui.screens.answer.common.ILikeActionCallback
import mago.apps.hertz.ui.screens.answer.common.QuestionContent
import mago.apps.hertz.ui.screens.answer.register.text.component.TagInfoContent
import mago.apps.hertz.ui.theme.light_sub_primary
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import mago.apps.hertz.ui.utils.compose.showToast
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnDefault

interface IFrequencyScoreCallback {
    fun onChanged(score: String)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerDetailScreen(
    navController: NavHostController,
    answerDetailViewModel: AnswerDetailViewModel,
    answerSeq: String?,
    answer: Answer?
) {
    answerDetailViewModel.apply {
        screenScrollState = rememberScrollState()
    }.run {
        LaunchedEffect(key1 = Unit, block = {
            answer?.let {
                answerDetailViewModel.updateAnswerState(it)
            } ?: run {
                answerSeq?.let {
                    answerDetailViewModel.getAnswerInfo(it.toInt())
                }
            }
        })
    }

    AnswerDetailScreenLifecycle(answerDetailViewModel)

    Scaffold(
        topBar = {
            AnswerDetailAppBar(navController, answerDetailViewModel)
        },
    ) {
        AnswerDetailContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(answerDetailViewModel.screenScrollState),
            answerDetailViewModel = answerDetailViewModel
        )
    }

    BBiBBiPopUp(answerDetailViewModel)
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
    BBiBBiErrorPopUp(answerDetailViewModel)
    BBiBBiSuccessPopUp(answerDetailViewModel)
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
    navController: NavHostController, answerDetailViewModel: AnswerDetailViewModel
) {
    val isEditingMode = answerDetailViewModel.isEditingMode.collectAsState().value

    AppBarContent(
        leftContent = {
            Icon(modifier = Modifier
                .size(40.dp)
                .noDuplicationClickable {
                    if (isEditingMode) {
                        answerDetailViewModel.updateEditingMode(false)
                    } else {
                        navController.popBackStack()
                    }
                }
                .padding(6.dp),
                imageVector = Icons.Default.ArrowBack,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null)
        },
        centerContent = {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = if (isEditingMode) {
                    stringResource(id = R.string.answer_detail_title_edit)
                } else {
                    stringResource(id = R.string.answer_detail_title)
                },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary
            )
        },
        rightContent = {
            Box(contentAlignment = Alignment.CenterEnd) {
                AnimatedVisibility(
                    visible = isEditingMode, enter = fadeIn(), exit = fadeOut()
                ) {
                    Text(modifier = Modifier
                        .noDuplicationClickable {
                            answerDetailViewModel.updateEditingMode(false)
                        }
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                        text = stringResource(id = R.string.save),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary)
                }
                AnimatedVisibility(
                    visible = !isEditingMode, enter = fadeIn(), exit = fadeOut()
                ) {
                    Icon(modifier = Modifier
                        .size(36.dp)
                        .noDuplicationClickable {
                            answerDetailViewModel.updateEditingMode(true)
                        }
                        .padding(6.dp),
                        imageVector = Icons.Outlined.Edit,
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = null)
                }
            }
        },
    )
}

@Composable
private fun AnswerDetailContent(
    modifier: Modifier, answerDetailViewModel: AnswerDetailViewModel
) {
    LoadingContent(answerDetailViewModel)
    ErrorContent(answerDetailViewModel)
    DetailContent(modifier, answerDetailViewModel)
}

@Composable
private fun DetailContent(modifier: Modifier, answerDetailViewModel: AnswerDetailViewModel) {

    val answerState = answerDetailViewModel.answerState.collectAsState().value
    val visibleState = MutableTransitionState(answerState.isSuccessState.value)

    AnimatedVisibility(visibleState = visibleState) {
        Column(modifier = modifier) {
            QuestionContent(
                content = answerState.data?.question?.text, backgroundColor = light_sub_primary
            )

            // 날짜 & 좋아요 영역
            DayAndLikeContent(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 20.dp, end = 20.dp),
                timeText = answerState.data?.createdAt,
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

            AnswerAudioContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                duration = answerDetailViewModel.getTime(answerState.data?.voice?.duration?.toInt()),
                voiceUrl = answerState.data?.voice?.voiceUrl.toString(),
                waveformImageUrl = answerState.data?.voice?.waveformUrl.toString(),
                answerDetailViewModel = answerDetailViewModel
            )

            AnswerText(
                answerDetailViewModel = answerDetailViewModel, text = answerState.data?.voice?.text
            )

            // 감정 주파수 %
            TodayFrequencyContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, start = 20.dp, end = 20.dp),
                answerDetailViewModel = answerDetailViewModel
            )

            TagInfoContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                answerDetailViewModel = answerDetailViewModel
            )

            BBiBBiFrequencyContent(
                answerState.data?.question?.questionSeq,
                answerState.data?.property,
                answerDetailViewModel
            ) // 삐삐전송하기, 우리의 감정주파수

        }
    }
}

@Composable
private fun AnswerAudioContent(
    modifier: Modifier,
    duration: String,
    voiceUrl: String,
    waveformImageUrl: String,
    answerDetailViewModel: AnswerDetailViewModel,
) {
    val isEditingMode = answerDetailViewModel.isEditingMode.collectAsState().value

    if (!isEditingMode && waveformImageUrl.isNotEmpty() && voiceUrl.isNotEmpty()) {
        AudioPlayLifecycle(voiceUrl, answerDetailViewModel)
        Row(
            modifier = modifier, verticalAlignment = Alignment.CenterVertically
        ) {
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
                    model = ImageRequest.Builder(LocalContext.current).data(waveformImageUrl)
                        .size(Size.ORIGINAL).crossfade(true).build()
                ),
                contentDescription = null,
            )

            AudioPlayIcon(answerDetailViewModel)
        }
    }
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
    AnimatedVisibility(visibleState = visibleState) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(id = R.string.answer_detail_error))
        }
    }
}

@Composable
private fun LoadingContent(answerDetailViewModel: AnswerDetailViewModel) {
    val answerState = answerDetailViewModel.answerState.collectAsState().value
    val visibleState = MutableTransitionState(answerState.isLoading.value)
    AnimatedVisibility(visibleState = visibleState) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(36.dp))
        }
    }
}

@Composable
private fun BBiBBiFrequencyContent(
    questionSeq: Int?, property: AnswerProperty?, answerDetailViewModel: AnswerDetailViewModel
) {
    val isEditingMode = answerDetailViewModel.isEditingMode.collectAsState().value

    if (!isEditingMode) {
        val boxModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary)

        property?.takeIf { !it.isSent }?.apply {
            BBiBBiButton(boxModifier, questionSeq, answerDetailViewModel)
        } ?: run {
            property?.takeIf { it.isConnected }?.apply {
                FrequencyButton(boxModifier)
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
private fun AnswerText(answerDetailViewModel: AnswerDetailViewModel, text: String?) {
    val isEditingMode = answerDetailViewModel.isEditingMode.collectAsState().value

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
        CustomTextField(
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.None),
            iTextCallback = object : ITextCallback {
                override fun renderText(content: String) {
                }
            },
            isSingleLine = false,
            defaultText = text.toString(),
            enable = isEditingMode,
        )
    }
}

@Composable
private fun TodayFrequencyContent(
    modifier: Modifier, answerDetailViewModel: AnswerDetailViewModel
) {
    val emotionList = answerDetailViewModel.frequencyInfoList
    val isEditingMode = answerDetailViewModel.isEditingMode.collectAsState().value
    val selectedValue = remember { mutableStateOf(EmotionType.HAPPINESS.name) }

    if (emotionList.isNotEmpty()) {
        Column(
            modifier = modifier,
        ) {
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

                    val icon = answerDetailViewModel.frequencyInfoList[idx].icon
                    val rate = answerDetailViewModel.frequencyInfoList[idx].rate
                    val type = answerDetailViewModel.frequencyInfoList[idx].iconType

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (isEditingMode) {
                            RadioButton(
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .size(20.dp),
                                selected = selectedValue.value == type,
                                onClick = {
                                    selectedValue.value = type
                                    answerDetailViewModel.updateSelectedFrequencyIndex(idx)
                                },
                            )
                        }
                        Text(
                            text = icon,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = "${rate.value}%",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
        if (isEditingMode) {
            Box {
                repeat(emotionList.size) { idx ->
                    FrequencySeekbar(
                        idx,
                        answerDetailViewModel,
                        object : IFrequencyScoreCallback {
                            override fun onChanged(score: String) {
                                answerDetailViewModel.updateFrequencyScore(score)
                            }
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FrequencySeekbar(
    idx: Int,
    answerDetailViewModel: AnswerDetailViewModel,
    iFrequencyScoreCallback: IFrequencyScoreCallback
) {
    val currentIndex = answerDetailViewModel.selectedFrequencyIndex.collectAsState().value

    var sliderPosition by remember {
        mutableStateOf(answerDetailViewModel.frequencyInfoList[idx].rate.value.toFloat())
    }
    val interactionSource = MutableInteractionSource()

    if (currentIndex == idx) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Slider(
                modifier = Modifier,
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                    iFrequencyScoreCallback.onChanged(sliderPosition.toInt().toString())
                },
                valueRange = 0f..100f,
                steps = 100,
                interactionSource = interactionSource,
                thumb = {
                    val shape = CircleShape
                    Spacer(
                        modifier = Modifier
                            .size(20.dp)
                            .indication(
                                interactionSource = interactionSource, indication = rememberRipple(
                                    bounded = false, radius = 20.dp
                                )
                            )
                            .hoverable(interactionSource = interactionSource)
                            .shadow(0.dp, shape, clip = false)
                            .background(MaterialTheme.colorScheme.primary, shape)
                    )
                },
            )
        }
    }
}

@Composable
private fun TagInfoContainer(
    modifier: Modifier, answerDetailViewModel: AnswerDetailViewModel
) {
    val isEditingMode = answerDetailViewModel.isEditingMode.collectAsState().value

    if (isEditingMode) {
        TagInfoContent(
            modifier = Modifier.padding(top = 12.dp, start = 20.dp, end = 20.dp),
            vm = answerDetailViewModel
        )
    } else {
        answerDetailViewModel.tagList.let {
            Column(
                modifier = modifier,
            ) {
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
                    itemsIndexed(items = it, key = { _, item -> item }) { _, item ->
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