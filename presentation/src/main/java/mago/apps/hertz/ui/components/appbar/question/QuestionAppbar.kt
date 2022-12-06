package mago.apps.hertz.ui.components.appbar.question

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.appbar.AppBarContent
import mago.apps.hertz.ui.components.appbar.AppbarType
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable

@Composable
fun QuestionAppbar() {
    AppBarContent(
        type = AppbarType.ICON_TITLE_ICON,
        textContent = {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = R.string.home_title_2),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        },
        leftContent = {
            Icon(
                modifier = Modifier.size(44.dp),
                painter = painterResource(id = R.drawable.profile_sample),
                contentDescription = null,
                tint = Color.Unspecified
            )
        },
        rightContent = {
            val rightIcons: List<Pair<ImageVector, () -> Unit>> = listOf(
                Pair(Icons.Default.Home, {}),
                Pair(Icons.Default.Menu, {}),
                Pair(Icons.Default.Notifications, {}),
            )

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
    )
}