package mago.apps.hertz.ui.screens.question

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import mago.apps.hertz.ui.components.appbar.icon_title_icons.IconTitleIconsAppbar
import mago.apps.hertz.ui.model.toast.TOAST_CODE_BACK_PRESS
import mago.apps.hertz.ui.screens.question.bottom.QuestionBottomBar
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import mago.apps.hertz.ui.utils.compose.showToast
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    navController: NavHostController,
    questionViewModel: QuestionViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        IconTitleIconsAppbar(navController = navController)
    }, bottomBar = {
        QuestionBottomBar(
            modifier = Modifier
                .fillMaxHeight(0.4f),
            navController = navController,
            questionViewModel = questionViewModel
        )
    }) {
        QuestionContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            questionViewModel = questionViewModel
        )
    }
    QuestionLifecycle(questionViewModel)
    BackPressEvent()
}

@Composable
private fun BackPressEvent() {
    val context = LocalContext.current
    var waitTime = remember { 0L }
    BackHandler(enabled = true) {
        if (System.currentTimeMillis() - waitTime >= 1500) {
            waitTime = System.currentTimeMillis()
            context.showToast(TOAST_CODE_BACK_PRESS)
        } else {
            (context as ComponentActivity).finish()
        }
    }
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
