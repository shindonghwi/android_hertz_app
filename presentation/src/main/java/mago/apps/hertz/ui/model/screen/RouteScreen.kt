package mago.apps.hertz.ui.model.screen

sealed class RouteScreen(val route: String) {

    // 홈
    object HomeScreen : RouteScreen("/home/")
    object NotificationsScreen : RouteScreen("/notifications/")
    object EpisodeListScreen : RouteScreen("/episode_list/")

    // 질문
    object QuestionScreen : RouteScreen("/question/")

    // 답변
    object AnswerAudioScreen : RouteScreen("/answer/register/audio")
    object AnswerTextScreen : RouteScreen("/answer/register/text")
    object AnswerDetailScreen : RouteScreen("/answer/detail")
    object AnswerEditScreen : RouteScreen("/answer/edit")
    object AnswerConnectedScreen : RouteScreen("/answer/connect/")
}
