package mago.apps.hertz.ui.screens.answer.detail

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.answer.AnswerEmotionList
import mago.apps.domain.model.answer.AnswerProperty
import mago.apps.domain.model.common.EmotionList
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.appbar.empty_title_text.EmptyTitleText
import mago.apps.hertz.ui.screens.answer.common.DayAndLikeContent
import mago.apps.hertz.ui.screens.answer.common.ILikeActionCallback
import mago.apps.hertz.ui.screens.answer.common.QuestionContent
import mago.apps.hertz.ui.theme.light_sub_primary
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import mago.apps.hertz.ui.utils.compose.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerDetailScreen(
    navController: NavHostController,
    answerDetailViewModel: AnswerDetailViewModel,
    answerSeq: String?,
    answer: Answer?
) {

    LaunchedEffect(key1 = Unit, block = {
        answer?.let {
            answerDetailViewModel.updateAnswerState(it)
        } ?: run {
            answerSeq?.let {
                answerDetailViewModel.getAnswerInfo(it.toInt())
            }
        }
    })

    Scaffold(topBar = { EmptyTitleText(action = {}) }) {
        AnswerDetailContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState()),
            navController = navController,
            answerDetailViewModel = answerDetailViewModel
        )
    }
}

@Composable
private fun AnswerDetailContent(
    modifier: Modifier,
    navController: NavHostController,
    answerDetailViewModel: AnswerDetailViewModel
) {
    LoadingContent(answerDetailViewModel)
    ErrorContent(answerDetailViewModel)
    DetailContent(modifier, answerDetailViewModel)
}

@Composable
private fun DetailContent(modifier: Modifier, answerDetailViewModel: AnswerDetailViewModel) {

    val context = LocalContext.current
    val answerState = answerDetailViewModel.answerState.collectAsState().value
    val visibleState = MutableTransitionState(answerState.isSuccessState.value)

    AnimatedVisibility(visibleState = visibleState) {
        Column(modifier = modifier) {
            QuestionContent(
                content = answerState.data?.question?.text,
                backgroundColor = light_sub_primary
            )

            // 날짜 & 좋아요 영역
            DayAndLikeContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 20.dp, end = 10.dp),
                timeText = answerState.data?.createdAt,
                likeDefaultState = answerState.data?.question?.isLiked,
                iLikeActionCallback = object : ILikeActionCallback {
                    override fun onState(likeState: Boolean) {
                        context.showToast("좋아요 기능. 미구현")
                    }
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .height(60.dp)
                    .background(Color.Red.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text("오디오 재생기능")
            }

            AnswerText(text = answerState.data?.voice?.text)

            // 감정 주파수 %
            TodayFrequencyContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, start = 20.dp, end = 20.dp),
                emotionList = answerState.data?.voice?.emotionList
            )

            TagInfoContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                tagList = answerState.data?.tagList
            )

            BBiBBiFrequencyButton(answerState.data?.property) // 삐삐전송하기, 우리의 감정주파수

        }
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
private fun BBiBBiFrequencyButton(property: AnswerProperty?) {

    val context = LocalContext.current

    val boxModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 14.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(MaterialTheme.colorScheme.primary)

    property?.takeIf { !it.isSent }?.apply {
        Box(
            modifier = boxModifier
                .then(Modifier.noDuplicationClickable {
                    context.showToast("삐삐전송. 미구현")
                })
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.answer_detail_bbibbi_button),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    } ?: run {
        property?.takeIf { it.isSent && !it.isConnected }?.apply {
            Box(
                modifier = boxModifier
                    .then(Modifier.noDuplicationClickable {
                        context.showToast("감정주파수 확인. 미구현")
                    })
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.answer_detail_connected_button),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@Composable
private fun AnswerText(text: String?) {
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
        Text(
            text = text ?: "",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun TodayFrequencyContent(
    modifier: Modifier,
    emotionList: List<AnswerEmotionList>?
) {
    emotionList?.let { itemList ->
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
                repeat(itemList.size) { idx ->

                    val type = itemList[idx].type

                    val icon = EmotionList.find { type == it.second.name }?.first.toString()
                    val iconType = EmotionList.find { type == it.second.name }?.second.toString()
                    val rate = "${itemList.find { it.type == iconType }?.rate}%"

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

@Composable
private fun TagInfoContent(modifier: Modifier, tagList: List<String>?) {
    tagList?.let {
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
                itemsIndexed(
                    items = it,
                    key = { _, item -> item }) { _, item ->
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