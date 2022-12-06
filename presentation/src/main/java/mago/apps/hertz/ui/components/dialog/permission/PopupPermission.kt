package mago.apps.hertz.ui.components.dialog.permission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mago.apps.hertz.R
import mago.apps.hertz.ui.theme.TitleXLarge
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable


@Composable
fun PopupPermission() {
    val configuration = LocalConfiguration.current
    val size = configuration.screenWidthDp.dp * 0.75f

    Column(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(top = 40.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 30.dp),
            text = stringResource(id = R.string.permission_mic_storage),
            style = MaterialTheme.typography.TitleXLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PopupPermissionButton(content = stringResource(id = R.string.allow))
            PopupPermissionButton(content = stringResource(id = R.string.deny), buttonAlpha = 0.6f)
        }
    }
}

@Composable
private fun PopupPermissionButton(
    content: String,
    buttonAlpha: Float = 1f
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = buttonAlpha))
            .noDuplicationClickable {
            }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = content,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .padding(8.dp),
                painter = painterResource(id = R.drawable.arrow_circle_up),
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = null,
            )
        }
    }
}