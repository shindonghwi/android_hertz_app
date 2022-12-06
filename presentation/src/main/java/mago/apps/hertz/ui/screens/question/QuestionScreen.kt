package mago.apps.hertz.ui.screens.question

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mago.apps.hertz.R
import mago.apps.hertz.ui.screens.question.bottom.QuestionBottomBar
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import java.util.*

@Composable
fun QuestionScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        QuestionContent(modifier = Modifier.weight(0.6f))
        QuestionBottomBar(modifier = Modifier.weight(0.4f), navController = navController)
    }
}

@Composable
private fun QuestionContent(modifier: Modifier) {
    val isVisible = remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        // 질문 영역
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            QuestionText(isVisible.value)
        }

        // 셔플 아이콘 영역
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp), contentAlignment = Alignment.BottomEnd
        ) {
            Icon(modifier = Modifier
                .size(36.dp)
                .noDuplicationClickable {
                    isVisible.value = !isVisible.value
                }
                .padding(6.dp),
                painter = painterResource(id = R.drawable.random),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuestionText(isVisible: Boolean) {

    val textList = listOf<String>(
        "내가 어른이 됐다고\n느낄 때는?",
        "당신이 가장 좋아했던\n사람은 누구인가요?",
        "지금 있는 곳에서\n누군가에게\n들려주고픈 노래는?",
        "인생이란 무엇인가?",
        "퇴근할때 듣고 싶은\n노래는 무엇인가요?",
        "맛있게 먹는 야식은\n살찐다 VS 안찐다",
    )
    val max_num_value = textList.size - 1
    val min_num_value = 0
    val randomText = textList[Random().nextInt(max_num_value - min_num_value + 1) + min_num_value]

    /** TODO: 질문 텍스트 동적 변경 필요 */
    AnimatedContent(
        targetState = isVisible,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(300)
            ) with fadeOut(
                animationSpec = tween(300)
            )
        },
    ) { targetState ->
        Text(
            modifier = Modifier.verticalScroll(state = rememberScrollState()),
            text = randomText,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight(
                    800
                )
            ),
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
    }
}
