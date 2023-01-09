package mago.apps.hertz.ui.screens.notification

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.appbar.AppBarContent
import mago.apps.hertz.ui.model.screen.RouteScreen
import mago.apps.hertz.ui.navigation.navigateTo
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable

@Composable
fun NotificationScreenScreen(
    navController: NavHostController,
    notificationsViewModel: NotificationsViewModel
) {
    Scaffold(
        topBar = {
            NotificationScreenAppbar(navController)
        }) {
        NotificationContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 20.dp),
            navController = navController,
            notificationsViewModel = notificationsViewModel
        )
    }
}

@Composable
private fun NotificationScreenAppbar(navController: NavHostController) {
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
                contentDescription = null
            )
        },
        centerContent = {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = R.string.notification_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary
            )
        },
    )
}

@Composable
private fun NotificationContent(
    modifier: Modifier,
    navController: NavHostController,
    notificationsViewModel: NotificationsViewModel
) {
    val notifications = notificationsViewModel.notifications.collectAsLazyPagingItems()

    AnimatedVisibility(
        visible = notifications.loadState.refresh is LoadState.NotLoading,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
        exit = fadeOut(animationSpec = tween(durationMillis = 250)),
    ) {
        if (notifications.loadState.append.endOfPaginationReached && notifications.itemCount < 1) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(id = R.string.notification_menu_empty))
            }
        } else {
            LazyColumn(
                modifier = modifier,
                state = rememberLazyListState(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(notifications, key = { item -> item.notificationSeq }) { item ->
                    NotificationItem(
                        navController = navController,
                        timeAgo = item?.timeAgo,
                        content = item?.message,
                        linkUrl = item?.link
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(
    navController: NavHostController,
    timeAgo: String?,
    content: String?,
    linkUrl: String?
) {

    val cardShape = RoundedCornerShape(10.dp)

    if (!content.isNullOrEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .shadow(
                    elevation = 5.dp,
                    shape = cardShape
                )
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = cardShape,
                )
                .noDuplicationClickable {
                    // 삐삐 메세지
                    if (linkUrl?.contains(RouteScreen.QuestionScreen.route) == true) {
                        navController.navigateTo(
                            RouteScreen.QuestionScreen.route + "?questionSeq=${
                                linkUrl.replace(
                                    RouteScreen.QuestionScreen.route,
                                    ""
                                )
                            }"
                        )
                    }
                    // 우리의 감정주파수 메세지
                    else if (linkUrl?.contains(RouteScreen.AnswerConnectedScreen.route) == true) {
                        navController.navigateTo(
                            RouteScreen.AnswerConnectedScreen.route + "?answerSeq=${
                                linkUrl.replace(
                                    RouteScreen.AnswerConnectedScreen.route,
                                    ""
                                )
                            }"
                        )
                    }

                }
                .padding(vertical = 8.dp, horizontal = 15.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = timeAgo ?: "",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.End
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .weight(1f),
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    text = content,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }

}
