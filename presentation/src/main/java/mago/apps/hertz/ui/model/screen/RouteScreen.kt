package mago.apps.hertz.ui.model.screen

sealed class RouteScreen(val route: String) {

    // 홈
    object HomeScreen : RouteScreen("Home")
    object NotificationScreen : RouteScreen("Notification")
    object EpisodeListScreen : RouteScreen("EpisodeList")

    // 질문
    object QuestionScreen : RouteScreen("Question")

    // 답변
    object AnswerAudioScreen : RouteScreen("AnswerAudio")
    object AnswerTextScreen : RouteScreen("AnswerText")
    object AnswerDetailScreen : RouteScreen("AnswerDetail")
    object AnswerEditScreen : RouteScreen("AnswerEdit")
    object AnswerConnectedScreen : RouteScreen("AnswerConnected")
}
