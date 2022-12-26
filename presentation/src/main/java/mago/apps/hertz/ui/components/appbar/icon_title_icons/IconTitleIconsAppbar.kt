package mago.apps.hertz.ui.components.appbar.icon_title_icons

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.appbar.AppBarContent
import mago.apps.hertz.ui.model.screen.RouteScreen
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable

@Composable
fun IconTitleIconsAppbar(navController: NavHostController) {
    AppBarContent(
        leftContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(44.dp),
                    painter = painterResource(id = R.drawable.profile_sample),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(id = R.string.home_title_2),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        rightContent = {
            val rightIcons: List<Pair<ImageVector, () -> Unit>> = listOf(
                Pair(Icons.Default.Home, {}),
                Pair(Icons.Default.Menu, {}),
                Pair(Icons.Default.Notifications) {
                    navController.navigate(RouteScreen.NotificationScreen.route) {
                        launchSingleTop = true
                    }
                },
            )
            Row(verticalAlignment = Alignment.CenterVertically){
                rightIcons.forEach {
                    Icon(modifier = Modifier
                        .size(40.dp)
                        .noDuplicationClickable {
                            it.second()
                        }
                        .padding(6.dp),
                        imageVector = it.first,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    )
}