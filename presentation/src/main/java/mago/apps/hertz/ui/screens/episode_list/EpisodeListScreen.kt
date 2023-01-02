package mago.apps.hertz.ui.screens.episode_list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.EmotionList
import mago.apps.hertz.R

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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        state = rememberLazyListState(),
        contentPadding = PaddingValues(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(myItemList, key = { item -> item.answerSeq }) { item ->
            DayLineText(dayText = item?.timeAgoDisplay)
            EpisodeMyItem(answerItem = item)
        }
        myItemList.apply {
            when (val currentState = loadState.refresh) {
                is LoadState.Loading -> {
                    Log.w("ASDasd", "EpisodeListScreen: loading")
                }
                is LoadState.Error -> {
                    val extractedException = currentState.error // SomeCatchableException
                    Log.w("ASDasd", "EpisodeListScreen: Error: $extractedException")
                }
                else -> {}
            }
        }
    }

}

@Composable
private fun EpisodeMyItem(answerItem: Answer?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp, shape = RoundedCornerShape(12.dp)
            )
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(start = 8.dp, end = 8.dp, top = 6.dp, bottom = 20.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = EmotionList.find { it.second.name == answerItem?.emotion }?.first.toString(),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = answerItem?.timeAgo.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
            )
        }

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = answerItem?.question?.text.toString(),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.secondary,
        )

        Row(
            modifier = Modifier
                .padding(top = 4.dp)
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
private fun DayLineText(dayText: String?) {
    if (!dayText.isNullOrEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier.weight(0.1f), color = MaterialTheme.colorScheme.outline
            )
            Text(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = dayText,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Divider(
                modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline
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
    LazyColumn {
        items(ourItemList) { item ->
            Text(
                text = item.toString(),
                style = MaterialTheme.typography.displayLarge,
                color = Color.Red
            )
        }
//        myItemList.apply {
//            when (val currentState = loadState.refresh) {
//                is LoadState.Loading -> {
//                    Log.w("ASDasd", "EpisodeListScreen: loading")
//                }
//                is LoadState.Error -> {
//                    val extractedException = currentState.error // SomeCatchableException
//                    Log.w("ASDasd", "EpisodeListScreen: Error: $extractedException")
//                }
//                else -> {}
//            }
//        }
    }

}


//LazyColumn {
//    items(userListItems) { item ->
//        Text(text = item.toString(), style = MaterialTheme.typography.displayLarge)
//    }
//    userListItems.apply {
//        when (val currentState = loadState.refresh) {
//            is LoadState.Loading -> {
//                Log.w("ASDasd", "EpisodeListScreen: loading")
//            }
//            is LoadState.Error -> {
//                val extractedException = currentState.error // SomeCatchableException
//                Log.w("ASDasd", "EpisodeListScreen: Error: $extractedException")
//            }
//            else -> {}
//        }
//    }
//}
