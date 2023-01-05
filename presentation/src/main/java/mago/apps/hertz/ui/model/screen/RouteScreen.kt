package mago.apps.hertz.ui.model.screen

sealed class RouteScreen(val route: String){
    object HomeScreen : RouteScreen("Home")
    object QuestionScreen : RouteScreen("Question")
    object AnswerAudioScreen : RouteScreen("AnswerAudio")
    object AnswerTextScreen : RouteScreen("AnswerText")
    object AnswerDetailScreen : RouteScreen("AnswerDetail")
    object AnswerEditScreen : RouteScreen("AnswerEdit")
    object NotificationScreen : RouteScreen("Notification")
    object EpisodeSaveScreen : RouteScreen("EpisodeSave")
    object EpisodeListScreen : RouteScreen("EpisodeList")
}
