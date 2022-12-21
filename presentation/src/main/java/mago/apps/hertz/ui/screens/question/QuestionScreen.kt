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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import mago.apps.hertz.R
import mago.apps.hertz.ui.screens.question.bottom.QuestionBottomBar
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnDefault

@Composable
fun QuestionScreen(
    navController: NavHostController,
    questionViewModel: QuestionViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        QuestionContent(
            modifier = Modifier.weight(0.6f),
            questionViewModel = questionViewModel
        )
        QuestionBottomBar(
            modifier = Modifier.weight(0.4f),
            navController = navController,
            questionViewModel = questionViewModel
        )
    }

    QuestionLifecycle(questionViewModel)
}

@Composable
private fun QuestionLifecycle(questionViewModel: QuestionViewModel) {
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(key1 = Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    coroutineScopeOnDefault { questionViewModel.fetchQuestion() }
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
private fun QuestionContent(modifier: Modifier, questionViewModel: QuestionViewModel) {
    val isVisible = questionViewModel.questionVisible.collectAsState().value

    Box(modifier = modifier) {
        // 질문 영역
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            QuestionText(isVisible, questionViewModel)
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
                    coroutineScopeOnDefault {
                        questionViewModel.fetchQuestion()
                    }
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
fun QuestionText(isVisible: Boolean, questionViewModel: QuestionViewModel) {

    val question = questionViewModel.currentQuestion.collectAsState().value

    AnimatedContent(
        targetState = isVisible,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(300)
            ) with fadeOut(
                animationSpec = tween(300)
            )
        },
    ) { _ ->
        Text(
            modifier = Modifier.verticalScroll(state = rememberScrollState()),
            text = question,
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
