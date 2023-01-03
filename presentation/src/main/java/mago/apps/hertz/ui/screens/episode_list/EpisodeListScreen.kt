package mago.apps.hertz.ui.screens.episode_list

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.EmotionList
import mago.apps.domain.model.common.ShareType
import mago.apps.hertz.R
import mago.apps.hertz.ui.model.screen.RouteScreen
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable

@Composable
fun EpisodeListScreen(
    navController: NavHostController, episodeListViewModel: EpisodeListViewModel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        EpisodeListContent(navController, episodeListViewModel)
    }
}

@Composable
private fun EpisodeListContent(
    navController: NavHostController, episodeListViewModel: EpisodeListViewModel
) {
    EpisodeTabList(navController, episodeListViewModel)
}

/** 탭 레이아웃 [ 내 에피소드, 우리의 감정주파수, 좋아요 ] */
@OptIn(ExperimentalPagerApi::class)
@Composable
private fun EpisodeTabList(
    navController: NavHostController, episodeListViewModel: EpisodeListViewModel
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val pages = listOf<String>(
        stringResource(id = R.string.episode_list_menu_1),
        stringResource(id = R.string.episode_list_menu_2),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            pages.forEachIndexed { index, title ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .selectable(selected = pagerState.currentPage == index, onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(page = index)
                            }
                        }),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight(800)),
                        textAlign = TextAlign.Center,
                        color = if (pagerState.currentPage == index) {
                            MaterialTheme.colorScheme.onBackground
                        } else {
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                        }
                    )
                }
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = MaterialTheme.colorScheme.outline
        )
    }

    HorizontalPager(
        count = pages.size,
        state = pagerState,
    ) { page ->
        when (page) {
            0 -> EpisodeMyItemList(navController, episodeListViewModel)
            1 -> EpisodeOurItemList(navController, episodeListViewModel)
        }
    }
}

/** 나의 고유주파수 아이템 */
@Composable
private fun EpisodeMyItemList(
    navController: NavHostController, episodeListViewModel: EpisodeListViewModel
) {
    val myItemList = episodeListViewModel.getAnswerMyList.collectAsLazyPagingItems()
    MyItemListEmptyView(myItemList, navController)
    MyItemListExistView(myItemList, navController)
}

@Composable
private fun MyItemListExistView(
    myItemList: LazyPagingItems<Answer>, navController: NavHostController
) {
    AnimatedVisibility(
        visible = myItemList.loadState.refresh is LoadState.NotLoading,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
        exit = fadeOut(animationSpec = tween(durationMillis = 250)),
    ) {
        if (myItemList.loadState.append.endOfPaginationReached && myItemList.itemCount < 1) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(id = R.string.episode_list_menu_1_empty))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = rememberLazyListState(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(myItemList, key = { item -> item.answerSeq }) { item ->
                    DayLineText(dayText = item?.firstDayInList)
                    EpisodeItem(answerItem = item, navController = navController)
                }
            }
        }
    }
}

@Composable
private fun OurItemListExistView(
    ourItemList: LazyPagingItems<Answer>, navController: NavHostController
) {
    AnimatedVisibility(
        visible = ourItemList.loadState.refresh is LoadState.NotLoading,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
        exit = fadeOut(animationSpec = tween(durationMillis = 250)),
    ) {
        if (ourItemList.loadState.append.endOfPaginationReached && ourItemList.itemCount < 1) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(id = R.string.episode_list_menu_2_empty))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = rememberLazyListState(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(ourItemList, key = { item -> item.answerSeq }) { item ->
                    DayLineText(dayText = item?.firstDayInList)
                    EpisodeItem(answerItem = item, navController = navController)
                }
            }
        }
    }
}

@Composable
private fun MyItemListEmptyView(
    myItemList: LazyPagingItems<Answer>, navController: NavHostController
) {
    AnimatedVisibility(
        visible = myItemList.loadState.refresh == LoadState.Loading,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
        exit = fadeOut(animationSpec = tween(durationMillis = 250)),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(36.dp))
        }
    }
}

@Composable
private fun OurItemListEmptyView(
    ourItemList: LazyPagingItems<Answer>, navController: NavHostController
) {
    AnimatedVisibility(
        visible = ourItemList.loadState.refresh == LoadState.Loading,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
        exit = fadeOut(animationSpec = tween(durationMillis = 250)),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(36.dp))
        }
    }
}

@Composable
private fun EpisodeItem(answerItem: Answer?, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.onPrimary)
            .noDuplicationClickable {
                Log.w("asdasdasd", "EpisodeItem: ${answerItem?.answerSeq}", )
                navController.navigate(
                    route = RouteScreen.AnswerDetailScreen.route +
                            "?answerSeq=${answerItem?.answerSeq}"
                ) {
                    launchSingleTop = true
                }
            }
            .padding(start = 8.dp, end = 8.dp, top = 6.dp, bottom = 30.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EmotionShareTimeAgo(answerItem = answerItem)
        }

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = answerItem?.question?.text.toString(),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.secondary,
        )

        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            answerItem?.tagList?.forEach {
                Text(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    text = "#${it}",
                    style = MaterialTheme.typography.labelSmall,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun EmotionShareTimeAgo(answerItem: Answer?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = EmotionList.find { it.second.name == answerItem?.emotion }?.first.toString(),
            style = MaterialTheme.typography.titleMedium,
        )

        Text(
            modifier = Modifier
                .padding(start = 4.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    when (answerItem?.shareType) {
                        ShareType.ALONE.name, ShareType.LINK.name -> {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        }
                        else -> {
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        }
                    }
                )
                .padding(vertical = 2.dp, horizontal = 4.dp),
            text = stringResource(
                id = when (answerItem?.shareType) {
                    ShareType.ALONE.name -> R.string.episode_list_label_type_my
                    ShareType.LINK.name -> R.string.episode_list_label_type_our
                    else -> R.string.episode_list_label_type_wait
                }
            ),
            style = MaterialTheme.typography.labelSmall,
            color = when (answerItem?.shareType) {
                ShareType.ALONE.name, ShareType.LINK.name -> MaterialTheme.colorScheme.onPrimary.copy(
                    alpha = 0.9f
                )
                else -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
            },
        )
    }

    Text(
        text = answerItem?.timeAgo.toString(),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
    )
}

@Composable
private fun DayLineText(dayText: String?) {
    if (!dayText.isNullOrEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier.weight(0.1f),
                color = MaterialTheme.colorScheme.outline,
            )
            Text(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = dayText,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight(600)
                ),
                color = MaterialTheme.colorScheme.secondary
            )
            Divider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

/** 우리의 감정주파수 아이템 */
@Composable
private fun EpisodeOurItemList(
    navController: NavHostController, episodeListViewModel: EpisodeListViewModel
) {
    val ourItemList = episodeListViewModel.getAnswerOurList.collectAsLazyPagingItems()
    OurItemListEmptyView(ourItemList, navController)
    OurItemListExistView(ourItemList, navController)
}
