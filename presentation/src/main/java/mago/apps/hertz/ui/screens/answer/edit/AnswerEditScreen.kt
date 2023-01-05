package mago.apps.hertz.ui.screens.answer.edit

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.answer.AnswerEmotion
import mago.apps.domain.model.common.EmotionType
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.appbar.AppBarContent
import mago.apps.hertz.ui.components.dialog.CustomPopup
import mago.apps.hertz.ui.components.dialog.IBackPressEvent
import mago.apps.hertz.ui.components.dialog.PopupType
import mago.apps.hertz.ui.components.input.CustomTextField
import mago.apps.hertz.ui.components.input.ITextCallback
import mago.apps.hertz.ui.model.screen.RouteScreen
import mago.apps.hertz.ui.model.toast.TOAST_CODE_FAIL_SCREEN_RELOAD
import mago.apps.hertz.ui.model.toast.TOAST_CODE_QUESTION_1
import mago.apps.hertz.ui.model.toast.TOAST_CODE_QUESTION_2
import mago.apps.hertz.ui.model.toast.TOAST_CODE_QUESTION_3
import mago.apps.hertz.ui.screens.answer.common.DayAndLikeContent
import mago.apps.hertz.ui.screens.answer.common.QuestionContent
import mago.apps.hertz.ui.screens.answer.edit.model.toAnswerData
import mago.apps.hertz.ui.screens.answer.register.text.component.TagInfoContent
import mago.apps.hertz.ui.theme.light_sub_primary
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import mago.apps.hertz.ui.utils.compose.showToast
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnDefault
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnMain

interface IFrequencyScoreCallback {
    fun onChanged(score: String)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerEditScreen(
    navController: NavHostController,
    answerEditViewModel: AnswerEditViewModel,
    answer: Answer
) {
    answerEditViewModel.apply {
        screenScrollState = rememberScrollState()
    }.run {
        initAnswerData(answer)
    }

    Scaffold(
        topBar = { AnswerEditAppBar(navController, answerEditViewModel) },
    ) {
        AnswerEditContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(answerEditViewModel.screenScrollState),
            answer = answer,
            answerEditViewModel = answerEditViewModel,
        )
    }

    AnswerLoadingView(answerEditViewModel)
    AnswerPatchErrorPopUp(answerEditViewModel)
    AnswerPatchSuccessPopUp(navController, answerEditViewModel)
}

@Composable
private fun AnswerLoadingView(answerEditViewModel: AnswerEditViewModel) {
    val patchState = answerEditViewModel.answerPatchState.collectAsState().value
    if (patchState.isLoading.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(36.dp))
        }
    }
}

@Composable
private fun AnswerPatchErrorPopUp(answerEditViewModel: AnswerEditViewModel) {
    val patchState = answerEditViewModel.answerPatchState.collectAsState().value
    if (patchState.isErrorState.value) {
        CustomPopup(
            isVisible = patchState.isErrorState,
            type = PopupType.FALLBACK,
            showingMessage = patchState.error
        )
    }
}

@Composable
private fun AnswerPatchSuccessPopUp(
    navController: NavHostController,
    answerEditViewModel: AnswerEditViewModel
) {
    val patchState = answerEditViewModel.answerPatchState.collectAsState().value
    val context = LocalContext.current

    CustomPopup(
        isVisible = patchState.isSuccessState,
        type = PopupType.REGISTER,
        showingMessage = stringResource(id = R.string.dialog_answer_patch_success),
        iBackPressEvent = object : IBackPressEvent {
            override fun onPress() {
                goToBackAndDataUpdate(context, navController, answerEditViewModel)
            }
        }
    )
}

@Composable
private fun AnswerEditContent(
    modifier: Modifier,
    answer: Answer,
    answerEditViewModel: AnswerEditViewModel
) {
    val questionText = answer.question.text
    val createdAt = answer.createdAt
    val emotionList = answer.voice?.emotionList
    val defaultText = answer.voice?.text

    Column(modifier = modifier) {
        QuestionContent(content = questionText, backgroundColor = light_sub_primary)

        // 날짜 & 좋아요 영역
        DayAndLikeContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 20.dp, end = 20.dp),
            timeText = createdAt,
            likeDefaultState = null,
        )

        AnswerText(defaultText = defaultText, answerEditViewModel = answerEditViewModel)

        // 감정 주파수 %
        TodayFrequencyContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp, start = 20.dp, end = 20.dp),
            emotionList = emotionList,
            answerEditViewModel = answerEditViewModel
        )

        TagInfoContent(
            modifier = Modifier.padding(top = 12.dp, start = 20.dp, end = 20.dp),
            vm = answerEditViewModel
        )
    }
}


@Composable
private fun AnswerEditAppBar(
    navController: NavHostController,
    answerEditViewModel: AnswerEditViewModel
) {
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
                text = stringResource(id = R.string.answer_detail_title_edit),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary
            )
        },
        rightContent = {
            Text(modifier = Modifier
                .noDuplicationClickable {
                    answerEditViewModel.run {
                        coroutineScopeOnDefault {
                            val response = patchAnswerData()
                            coroutineScopeOnMain {
                                when (response) {
                                    // 등록 하지 못하는 질문 유형
                                    TOAST_CODE_QUESTION_1 -> {
                                        context.showToast(TOAST_CODE_QUESTION_1)
                                        navController.popBackStack()
                                    }
                                    TOAST_CODE_QUESTION_2 -> {
                                        context.showToast(TOAST_CODE_QUESTION_2)
                                    }
                                    TOAST_CODE_QUESTION_3 -> {
                                        context.showToast(TOAST_CODE_QUESTION_3)
                                    }
                                }
                            }
                        }
                    }
                }
                .padding(horizontal = 8.dp, vertical = 6.dp),
                text = stringResource(id = R.string.save),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary)
        },
    )
}


@Composable
private fun AnswerText(defaultText: String?, answerEditViewModel: AnswerEditViewModel) {
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
                    answerEditViewModel.updatePatchAnswerText(content)
                }
            },
            isSingleLine = false,
            defaultText = defaultText ?: "",
            enable = true,
        )
    }
}

@Composable
private fun TodayFrequencyContent(
    modifier: Modifier,
    emotionList: List<AnswerEmotion>?,
    answerEditViewModel: AnswerEditViewModel,
) {
    val selectedValue = remember { mutableStateOf(EmotionType.HAPPINESS.name) }

    if (!emotionList.isNullOrEmpty()) {
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

                    val icon = answerEditViewModel.frequencyInfoList[idx].icon
                    val rate = answerEditViewModel.frequencyInfoList[idx].rate.value
                    val type = answerEditViewModel.frequencyInfoList[idx].iconType

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .size(20.dp),
                            selected = selectedValue.value == type,
                            onClick = {
                                selectedValue.value = type
                                answerEditViewModel.updateSelectedFrequencyIndex(idx)
                            },
                        )
                        Text(
                            text = icon,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = "${rate}%",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
        Box {
            repeat(emotionList.size) { idx ->
                FrequencySeekbar(
                    idx,
                    emotionList,
                    answerEditViewModel,
                    object : IFrequencyScoreCallback {
                        override fun onChanged(score: String) {
                            answerEditViewModel.run {
                                updateFrequencyScore(score)
                                updatePatchAnswerEmotion(
                                    emotionList[idx].type,
                                    score.toInt()
                                )
                            }
                        }
                    },
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FrequencySeekbar(
    idx: Int,
    emotionList: List<AnswerEmotion>?,
    answerEditViewModel: AnswerEditViewModel,
    iFrequencyScoreCallback: IFrequencyScoreCallback
) {
    val currentIndex = answerEditViewModel.selectedFrequencyIndex.collectAsState().value

    var sliderPosition by remember {
        mutableStateOf(emotionList?.get(idx)?.rate?.toFloat() ?: 0f)
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

fun goToBackAndDataUpdate(
    context: Context,
    navController: NavHostController,
    answerEditViewModel: AnswerEditViewModel
) {
    answerEditViewModel.run {
        answerData?.let { answer ->
            val newAnswerData = answerPatchData?.toAnswerData(answer)
            navController.navigate(
                route = RouteScreen.AnswerDetailScreen.route +
                        "?answer=${Gson().toJson(newAnswerData)}",
            ) {
                popUpTo(RouteScreen.EpisodeListScreen.route)
            }
        } ?: run {
            context.showToast(TOAST_CODE_FAIL_SCREEN_RELOAD)
        }
    }
}