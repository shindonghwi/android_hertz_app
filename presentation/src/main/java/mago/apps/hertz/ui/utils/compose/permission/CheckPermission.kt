package mago.apps.hertz.ui.utils.compose.permission

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mago.apps.hertz.R

@Composable
fun CheckPermission() {
    val configuration = LocalConfiguration.current
    val popUpSize = configuration.screenHeightDp.dp * 0.40f

    val infoDialog = remember { mutableStateOf(true) }

    if (infoDialog.value) {
        AnimatedVisibility(
            visible = infoDialog.value,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = Modifier
                        .size(popUpSize)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 20.dp, vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(52.dp),
                        painter = painterResource(id = R.drawable.profile_sample),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )

                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        text = stringResource(id = R.string.permission_description),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier.padding(top = 40.dp),
                        text = stringResource(id = R.string.permission_read_storage),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight(600)
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = stringResource(id = R.string.permission_mic_storage),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight(600)
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
    BackHandler(enabled = infoDialog.value) {
        infoDialog.value = false
    }
}
