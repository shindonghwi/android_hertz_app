package mago.apps.hertz.ui.screens.episode_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import mago.apps.hertz.R

@Composable
fun EpisodeListScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        EpisodeListContent()
    }
}

@Composable
private fun EpisodeListContent() {
    EpisodeTabList()
}

/** 탭 레이아웃 [ 내 에피소드, 우리의 감정주파수, 좋아요 ] */
@OptIn(ExperimentalPagerApi::class)
@Composable
private fun EpisodeTabList() {
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
                        .selectable(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(page = index)
                                }
                            }
                        ),
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
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            repeat(300) {
                Text(text = page.toString())
            }
        }
    }
}

/** 아이템 목록 */
@Composable
private fun EpisodeListView() {

}

/** 나의 고유주파수 아이템 */
@Composable
private fun EpisodeMyItem() {

}

/** 우리의 감정주파수 아이템 */
@Composable
private fun EpisodeOurItem() {

}
