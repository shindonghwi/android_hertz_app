package mago.apps.hertz.ui.components.refresh

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefreshIndicatorTransform
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeRefreshIndicator(
    modifier: Modifier = Modifier, state: PullRefreshState, refreshing: Boolean
) {
    val indicatorSize = 40.dp

    Surface(
        modifier = modifier
            .size(indicatorSize)
            .pullRefreshIndicatorTransform(state, true),
        shape = CircleShape,
        shadowElevation = if (refreshing) 16.dp else 0.dp,
    ) {
        if (refreshing) {
            val transition = rememberInfiniteTransition()
            val degree by transition.animateFloat(
                initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1000, easing = LinearEasing
                    )
                )
            )
            Icon(
                modifier = Modifier.rotate(degree),
                imageVector = Icons.Default.Refresh,
                contentDescription = "refresh",
                tint = MaterialTheme.colorScheme.primary
            )
        } else {
            Icon(
                modifier = Modifier.rotate(state.progress * 180),
                imageVector = Icons.Default.Refresh,
                contentDescription = "refresh",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}