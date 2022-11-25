package mago.apps.hertz.ui.data.model

sealed class RouteScreen(val route: String){
    object HomeScreen : RouteScreen("Home")
    object MatchingScreen : RouteScreen("Matching")
    object QuestionScreen : RouteScreen("Question")
    object CategoryScreen : RouteScreen("Category")
}
