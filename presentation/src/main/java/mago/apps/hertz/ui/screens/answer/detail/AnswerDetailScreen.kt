package mago.apps.hertz.ui.screens.answer.detail

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.EmotionList
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.appbar.empty_title_text.EmptyTitleText
import mago.apps.hertz.ui.screens.answer.common.DayAndLikeContent
import mago.apps.hertz.ui.screens.answer.common.ILikeActionCallback
import mago.apps.hertz.ui.screens.answer.common.QuestionContent
import mago.apps.hertz.ui.theme.light_sub_primary
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerDetailScreen(
    navController: NavHostController,
    answerDetailViewModel: AnswerDetailViewModel,
    answer: Answer?
) {
    LaunchedEffect(key1 = Unit, block = {
        answer?.let { answerDetailViewModel.updateAnswerInfo(it) }
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
    Column(modifier = modifier) {
        QuestionContent(
            content = answerDetailViewModel.answerInfo?.question?.text,
            backgroundColor = light_sub_primary
        )

        // 날짜 & 좋아요 영역
        DayAndLikeContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 10.dp),
            timeText = answerDetailViewModel.answerInfo?.createdAt,
            likeDefaultState = false,
            iLikeActionCallback = object : ILikeActionCallback {
                override fun onState(likeState: Boolean) {
                    Log.w("asdasd", "onState: $likeState")
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.Red.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text("오디오 재생기능")
        }

        AnswerText(text = answerDetailViewModel.answerInfo?.voice?.text)

        // 감정 주파수 %
        TodayFrequencyContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, start = 20.dp, end = 20.dp),
        )

        TagInfoContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            tagList = listOf(
                "새해",
                "맑은하늘",
                "아이스아메리카노",
                "달빛",
                "검은토끼"
            )
        )

    }
}

@Composable
private fun AnswerText(text: String?) {
    Box(
        modifier = Modifier
            .padding(top = 13.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(9.dp))
            .background(light_sub_primary)
            .padding(14.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = text ?: "",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun TodayFrequencyContent(modifier: Modifier) {
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
            repeat(EmotionList.size) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = EmotionList[it].first,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = "${(Random.nextInt(100) + 1)}%",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}

@Composable
private fun TagInfoContent(modifier: Modifier, tagList: List<String>) {
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
                items = tagList,
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