package mago.apps.hertz.ui.screens.answer.text

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mago.apps.domain.model.common.EmotionType
import mago.apps.domain.model.question.QuestionRandom
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.appbar.empty_title_text.EmptyTitleText
import mago.apps.hertz.ui.components.dialog.CustomPopup
import mago.apps.hertz.ui.components.dialog.PopupType
import mago.apps.hertz.ui.components.input.CustomTextField
import mago.apps.hertz.ui.components.input.ITextCallback
import mago.apps.hertz.ui.components.input.KeyBoardActionUnit
import mago.apps.hertz.ui.model.screen.RouteScreen
import mago.apps.hertz.ui.model.toast.TOAST_CODE_QUESTION_1
import mago.apps.hertz.ui.model.toast.TOAST_CODE_QUESTION_2
import mago.apps.hertz.ui.model.toast.TOAST_CODE_QUESTION_3
import mago.apps.hertz.ui.screens.answer.text.common.QuestionContent
import mago.apps.hertz.ui.screens.answer.text.common.TagInfo
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import mago.apps.hertz.ui.utils.compose.showToast
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnMain
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerTextScreen(
    navController: NavHostController,
    answerTextViewModel: AnswerTextViewModel,
    question: QuestionRandom,
) {
    val context = LocalContext.current

    answerTextViewModel.run {
        updateQuestionInfo(question)
        screenScrollState = rememberScrollState()
    }

    Scaffold(topBar = {
        EmptyTitleText(action = {
            answerTextViewModel.run {
                coroutineScopeOnMain {
                    when (postAnswerText()) {
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
        })
    }) {
        AnswerTextContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(answerTextViewModel.screenScrollState),
            answerTextViewModel = answerTextViewModel,
            navController = navController
        )
    }
}

@Composable
private fun AnswerTextContent(
    modifier: Modifier,
    answerTextViewModel: AnswerTextViewModel,
    navController: NavHostController
) {
    Column(modifier = modifier) {
        QuestionContent(answerTextViewModel.questionInfo?.text)

        // 날짜 & 좋아요 영역
        DayAndLikeContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 10.dp),
        )

        // 답변 영역
        InputAnswer(
            modifier = Modifier
                .padding(top = 13.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
                .padding(14.dp), answerTextViewModel = answerTextViewModel
        )

        // 감정 주파수 선택
        TodayFrequencyTitle(
            modifier = Modifier.padding(top = 30.dp, start = 20.dp, end = 20.dp),
            answerTextViewModel = answerTextViewModel
        )

        // 태그 정보
        TagInfo(
            modifier = Modifier.padding(top = 12.dp, start = 20.dp, end = 20.dp),
            answerTextViewModel = answerTextViewModel
        )
    }
    PostAnswerTextPopup(answerTextViewModel, navController)
}


@Composable
private fun DayAndLikeContent(modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = SimpleDateFormat(
                "yyyy년 MM월 dd일 EE요일", Locale.getDefault()
            ).format(Calendar.getInstance().time),
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun InputAnswer(modifier: Modifier, answerTextViewModel: AnswerTextViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current

    CustomTextField(
        modifier = modifier,
        textStyle = MaterialTheme.typography.bodyLarge,
        placeholderText = {
            Text(
                text = stringResource(id = R.string.answer_text_placeholder),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                textAlign = TextAlign.Center
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyBoardActionUnit = KeyBoardActionUnit(onDone = { keyboardController?.hide() }),
        iTextCallback = object : ITextCallback {
            override fun renderText(content: String) {
                answerTextViewModel.updateCurrentEditingText(content)
            }
        },
    )
}


@Composable
private fun TodayFrequencyTitle(modifier: Modifier, answerTextViewModel: AnswerTextViewModel) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(id = R.string.answer_text_title_today_emotion_frequency),
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        EmotionPercentSelectView(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            answerTextViewModel = answerTextViewModel
        )
    }
}

@Composable
private fun EmotionPercentSelectView(modifier: Modifier, answerTextViewModel: AnswerTextViewModel) {
    val selectedValue = remember { mutableStateOf(EmotionType.HAPPINESS) }
    val emotionList = listOf(
        Pair("\uD83D\uDE04", EmotionType.HAPPINESS),
        Pair("\uD83D\uDE22", EmotionType.SADNESS),
        Pair("\uD83D\uDE21", EmotionType.ANGRY),
        Pair("\uD83D\uDE36", EmotionType.NEUTRAL),
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
                RadioButton(
                    selected = selectedValue.value == emotionList[it].second,
                    onClick = {
                        answerTextViewModel.updateEmotion(emotionList[it].second)
                        selectedValue.value = emotionList[it].second
                    },
                )
            }
        }
    }
}

@Composable
private fun PostAnswerTextPopup(
    answerTextViewModel: AnswerTextViewModel,
    navController: NavHostController
) {
    val answerVoiceState = answerTextViewModel.postAnswerTextState.collectAsState().value

    CustomPopup(
        isVisible = answerVoiceState.isLoading,
        backgroundTouchEnable = true,
        type = PopupType.REGISTER,
        showingMessage = "답변을 등록중입니다",
    )

    LaunchedEffect(key1 = answerVoiceState, block = {
        if (answerVoiceState.isSuccessState.value) {
            navController.navigate(route = RouteScreen.EpisodeListScreen.route){
                popUpTo(RouteScreen.QuestionScreen.route)
            }
        }
    })
}