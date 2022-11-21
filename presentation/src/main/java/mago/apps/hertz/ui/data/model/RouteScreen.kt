package mago.apps.hertz.ui.data.model

sealed class RouteScreen(val route: String){
    object HomeScreen : RouteScreen("Home")
    object QuestionScreen : RouteScreen("Question")
}
