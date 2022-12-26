package mago.apps.hertz.ui.components.appbar

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mago.apps.hertz.ui.components.appbar.empty_title_text.EmptyTitleText
import mago.apps.hertz.ui.components.appbar.icon_title_icons.IconTitleIconsAppbar
import mago.apps.hertz.ui.model.screen.RouteScreen

@Composable
fun AppBar(currentRoute: String?, navController: NavHostController) {
    currentRoute?.let { route ->
        if (route.contains(RouteScreen.AnswerTextScreen.route)) {
            EmptyTitleText(navController)
        } else {
            when (route) {
                RouteScreen.HomeScreen.route -> {}
                else -> {
                    IconTitleIconsAppbar(navController)
                }
            }
        }
    }
}

@Composable
fun AppBarContent(
    centerContent: @Composable (() -> Unit)? = null,
    leftContent: @Composable (() -> Unit)? = null,
    rightContent: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp), contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp)
                .background(
                    if (isSystemInDarkTheme()) {
                        Color.Black.copy(alpha = 0.0f)
                    } else {
                        Color.White.copy(alpha = 0.3f)
                    }
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            leftContent?.let { it() } ?: apply { Spacer(modifier = Modifier.weight(1f)) }
            rightContent?.let { it() }
        }

        centerContent?.let { it() }
    }
}