package mago.apps.hertz.ui.model.screen

sealed class RouteScreen(val route: String){
    object HomeScreen : RouteScreen("Home")
    object QuestionScreen : RouteScreen("Question")
    object CategoryScreen : RouteScreen("Category")
    object AnswerAudioScreen : RouteScreen("AnswerAudio")
    object AnswerTextScreen : RouteScreen("AnswerText")
    object NotificationScreen : RouteScreen("Notification")
    object EpisodeSaveScreen : RouteScreen("EpisodeSave")
}
