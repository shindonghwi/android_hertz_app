package mago.apps.hertz.ui.screens.answer.connected

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.appbar.AppBarContent
import mago.apps.hertz.ui.screens.answer.common.DayAndLikeContent
import mago.apps.hertz.ui.screens.answer.common.ILikeActionCallback
import mago.apps.hertz.ui.screens.answer.common.QuestionContent
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
//                    coroutineScopeOnDefault {
//                        if (likeState) {
//                            answerDetailViewModel.postLike(it.question.questionSeq)
//                        } else {
//                            answerDetailViewModel.delLike(it.question.questionSeq)
//                        }
//                    }
                }
            }
        })

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