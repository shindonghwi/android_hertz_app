package mago.apps.hertz.ui.screens.episode_list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
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
    EpisodeMyItemList(navController, episodeListViewModel)
//    EpisodeTabList(navController, episodeListViewModel)
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
    navController: NavHostController,
    episodeListViewModel: EpisodeListViewModel
) {
    val myItemList = episodeListViewModel.getAnswerMyList.collectAsLazyPagingItems()

    LazyColumn(modifier = Modifier.fillMaxSize(), state = rememberLazyListState()) {
        itemsIndexed(myItemList, key = { index, item -> index }) { index, item ->

            item?.timeAgoDisplay?.let {
                if (it.isNotEmpty()){
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Red
                    )
                }
            }

            Text(
                text = item?.question?.text.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Red
            )
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
