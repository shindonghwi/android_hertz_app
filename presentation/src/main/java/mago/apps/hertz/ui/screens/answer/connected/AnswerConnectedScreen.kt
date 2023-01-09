package mago.apps.hertz.ui.screens.answer.connected

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mago.apps.domain.model.answer.AnswerCommon
import mago.apps.domain.model.answer.AnswerVoice
import mago.apps.domain.model.common.EmotionList
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.appbar.AppBarContent
import mago.apps.hertz.ui.screens.answer.common.DayAndLikeContent
import mago.apps.hertz.ui.screens.answer.common.ILikeActionCallback
import mago.apps.hertz.ui.screens.answer.common.QuestionContent
import mago.apps.hertz.ui.theme.light_sub_primary
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerConnectedScreen(
    navController: NavHostController,
    answerConnectedViewModel: AnswerConnectedViewModel,
    answerSeq: String?,
) {
    LaunchedEffect(key1 = Unit, block = {
        answerSeq?.toIntOrNull()?.let { answerConnectedViewModel.getAnswerConnectedInfo(it) }
    })

    Scaffold(
        topBar = { AnswerConnectedAppBar(navController) },
    ) {
        AnswerConnectedContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState()),
            answerConnectedViewModel = answerConnectedViewModel
        )
    }
}

@Composable
private fun AnswerConnectedContent(
    modifier: Modifier,
    answerConnectedViewModel: AnswerConnectedViewModel
) {
    val answerConnectedState = answerConnectedViewModel.answerConnectedState.collectAsState().value
    val visibleState = MutableTransitionState(answerConnectedState.isSuccessState.value)

    Column(modifier = modifier) {

        QuestionContent(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
                .padding(14.dp),
            content = answerConnectedState.data?.question?.text ?: ""
        )

        // 날짜 & 좋아요 영역
        DayAndLikeContent(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 20.dp, end = 20.dp),
            timeText = answerConnectedState.data?.createdAt,
            visibleState = visibleState,
            likeDefaultState = answerConnectedState.data?.question?.isLiked,
            iLikeActionCallback = object : ILikeActionCallback {
                override fun onState(likeState: Boolean) {
                    answerConnectedState.data?.let {
                        coroutineScopeOnDefault {
                            answerConnectedViewModel.run {
                                if (likeState) {
                                    postLike(it.question.questionSeq)
                                } else {
                                    delLike(it.question.questionSeq)
                                }
                            }

                        }
                    }
                }
            })

        AnswerCommonData(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            common = answerConnectedState.data?.common
        )

        answerConnectedState.data?.voiceList?.first()?.let {
            AnswerDataCard(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(light_sub_primary)
                    .padding(vertical = 20.dp, horizontal = 14.dp),
                answerVoice = it,
                isMe = true
            )
        }

        answerConnectedState.data?.voiceList?.last()?.let {
            AnswerDataCard(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .padding(20.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFFACE))
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                answerVoice = it,
                isMe = false
            )
        }
    }

}

@Composable
private fun AnswerDataCard(modifier: Modifier, answerVoice: AnswerVoice?, isMe: Boolean) {
    Column(modifier = modifier) {

        answerVoice?.text?.let {
            Text(
                text = if (isMe) {
                    stringResource(id = R.string.answer_connected_me_data_title_answer)
                } else {
                    stringResource(id = R.string.answer_connected_opponent_data_title_answer)
                },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = it,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }

        answerVoice?.emotionList?.let { emotionList ->
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = if (isMe) {
                    stringResource(id = R.string.answer_connected_me_data_title_frequency)
                } else {
                    stringResource(id = R.string.answer_connected_opponent_data_title_frequency)
                },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
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

        answerVoice?.tagList?.let { tagList ->
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = if (isMe) {
                    stringResource(id = R.string.answer_connected_me_data_title_tag)
                } else {
                    stringResource(id = R.string.answer_connected_opponent_data_title_tag)
                },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
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
                            color = MaterialTheme.colorScheme.tertiary,
                            style = MaterialTheme.typography.labelMedium,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

        }

        answerVoice?.voiceUrl?.let {
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = if (isMe) {
                    stringResource(id = R.string.answer_connected_me_data_title_voice)
                } else {
                    stringResource(id = R.string.answer_connected_opponent_data_title_voice)
                },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }

    }
}


@Composable
private fun AnswerConnectedAppBar(
    navController: NavHostController,
) {
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
                text = stringResource(id = R.string.answer_connected_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    )
}

@Composable
private fun AnswerCommonData(modifier: Modifier, common: AnswerCommon?) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        common?.emotion?.let { emotion ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.answer_connected_common_menu_frequency),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = EmotionList.find { it.second.name == emotion.type }?.first ?: "",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = "${emotion.rate}%",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }

        common?.keywordList?.let { keywordList ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.answer_connected_common_menu_keyword),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = keywordList.firstOrNull() ?: "",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}