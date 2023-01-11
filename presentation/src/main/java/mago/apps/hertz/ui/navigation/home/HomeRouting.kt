package mago.apps.hertz.ui.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import mago.apps.hertz.ui.model.screen.RouteScreen
import mago.apps.hertz.ui.screens.episode_list.EpisodeListScreen
import mago.apps.hertz.ui.screens.episode_list.EpisodeListViewModel
import mago.apps.hertz.ui.screens.home.HomeScreen
import mago.apps.hertz.ui.screens.home.HomeViewModel
import mago.apps.hertz.ui.screens.notification.NotificationScreenScreen
import mago.apps.hertz.ui.screens.notification.NotificationsViewModel

fun NavGraphBuilder.homeRouting(navController: NavHostController) {

    /** @feature: 홈 화면 (로그인)
     * @author: 2023/01/09 3:45 PM donghwishin
     */
    composable(route = RouteScreen.HomeScreen.route) {
        val homeViewModel = hiltViewModel<HomeViewModel>()
        HomeScreen(navController, homeViewModel)
    }

    /** @feature: 에피소드 리스트 화면
     * @author: 2023/01/09 3:45 PM donghwishin
     */
    composable(route = RouteScreen.EpisodeListScreen.route) {
        val episodeListViewModel = hiltViewModel<EpisodeListViewModel>()
        EpisodeListScreen(navController, episodeListViewModel)
    }

    /** @feature: 알림 목록화면
     * @author: 2023/01/09 3:45 PM donghwishin
     */
    composable(route = RouteScreen.NotificationsScreen.route) {
        val notificationsViewModel = hiltViewModel<NotificationsViewModel>()
        NotificationScreenScreen(navController, notificationsViewModel)
    }
}