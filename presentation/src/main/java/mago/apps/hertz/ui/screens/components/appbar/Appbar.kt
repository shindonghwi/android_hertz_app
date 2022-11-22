package mago.apps.hertz.ui.screens.components.appbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppBar(
    title: String = "",
    type: AppbarType,
    leftContent: @Composable (() -> Unit)? = null,
    rightContent: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 12.dp)
            .background(
                if (isSystemInDarkTheme()) {
                    Color.Black.copy(alpha = 0.0f)
                } else {
                    Color.White.copy(alpha = 0.3f)
                }
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // left area
            LeftAppBar(type, title, leftContent)

            // center area
            CenterAppBar(type, title)

            Spacer(modifier = Modifier.weight(1f))

            // right area
            RightAppBar(type, rightContent)
        }
    }
}

@Composable
private fun RowScope.LeftAppBar(
    type: AppbarType, title: String = "", leftContent: @Composable (() -> Unit)?
) {
    when (type) {
        AppbarType.EMPTY -> {}
        AppbarType.PROFILE_TITLE_LEFT -> {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .border(1.dp, Color.Black, CircleShape),
                imageVector = Icons.Rounded.Person,
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        AppbarType.ICON_TITLE_ICON -> {
            leftContent?.let { it() }
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        else -> {}
    }
}

@Composable
fun RowScope.CenterAppBar(type: AppbarType, title: String) {
    if (type == AppbarType.ONLY_TITLE_CENTER) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun RowScope.RightAppBar(
    type: AppbarType, rightContent: @Composable() (() -> Unit)?
) {
    if (type == AppbarType.ICON_TITLE_ICON) {
        rightContent?.let { it() }
    }
}