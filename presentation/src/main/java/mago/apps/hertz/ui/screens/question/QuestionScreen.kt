package mago.apps.hertz.ui.screens.question

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.appbar.AppBarContent
import mago.apps.hertz.ui.model.screen.RouteScreen
import mago.apps.hertz.ui.model.toast.TOAST_CODE_BACK_PRESS
import mago.apps.hertz.ui.screens.question.bottom.QuestionBottomBar
import mago.apps.hertz.ui.utils.compose.findMainActivity
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import mago.apps.hertz.ui.utils.compose.showToast
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    navController: NavHostController,
    questionViewModel: QuestionViewModel,
    questionSeq: Int?,
) {
    Scaffold(topBar = { QuestionAppBar(navController) }, bottomBar = {
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
            questionViewModel = questionViewModel,
        )
    }
    QuestionLifecycle(questionViewModel, questionSeq)
    BackPressEvent()
}

@Composable
private fun QuestionAppBar(navController: NavHostController) {
    AppBarContent(
        leftContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(44.dp),
                    painter = painterResource(id = R.drawable.profile_sample),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(id = R.string.home_title_2),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        rightContent = {
            val rightIcons: List<Pair<ImageVector, () -> Unit>> = listOf(
                Pair(Icons.Default.Menu) {
                    navController.navigate(RouteScreen.EpisodeListScreen.route) {
                        launchSingleTop = true
                    }
                },
                Pair(Icons.Default.Notifications) {
                    navController.navigate(RouteScreen.NotificationScreen.route) {
                        launchSingleTop = true
                    }
                },
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                rightIcons.forEach {
                    Icon(modifier = Modifier
                        .size(40.dp)
                        .noDuplicationClickable {
                            it.second()
                        }
                        .padding(6.dp),
                        imageVector = it.first,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    )
}

@Composable
private fun BackPressEvent() {
    val activity = LocalContext.current.findMainActivity()
    var waitTime = remember { 0L }
    BackHandler(enabled = true) {
        if (System.currentTimeMillis() - waitTime >= 1500) {
            waitTime = System.currentTimeMillis()
            activity.showToast(TOAST_CODE_BACK_PRESS)
        } else {
            activity.finish()
        }
    }
}

@Composable
private fun QuestionLifecycle(questionViewModel: QuestionViewModel, questionSeq: Int?) {
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(key1 = Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    coroutineScopeOnDefault {
                        questionViewModel.run {
                            questionSeq?.let { seq ->
                                getQuestionInfo(seq)
                            } ?: run {
                                fetchQuestion()
                            }
                        }
                    }
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
private fun QuestionContent(
    modifier: Modifier,
    questionViewModel: QuestionViewModel,
) {
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
    ) {
        Column {

            question?.property?.let {
                Box(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        text = String.format(
                            stringResource(id = R.string.qusetion_opponent_name),
                            it.name
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Text(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .verticalScroll(state = rememberScrollState()),
                text = question?.text ?: "",
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
}
