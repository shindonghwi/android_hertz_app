package mago.apps.hertz.ui.components.appbar.empty_title_text

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.appbar.AppBarContent
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable

@Composable
fun EmptyTitleText(action: (() -> Unit)? = null) {
    AppBarContent(
        centerContent = {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = R.string.home_title_2),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        },
        rightContent = {
            Text(
                modifier = Modifier
                    .noDuplicationClickable { action?.let { it() } }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                text = stringResource(id = R.string.save),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }
    )
}