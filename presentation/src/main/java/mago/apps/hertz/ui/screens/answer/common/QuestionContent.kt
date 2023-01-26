package mago.apps.hertz.ui.screens.answer.common


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun QuestionContent(
    modifier: Modifier,
    content: String?,
    visibleState: MutableTransitionState<Boolean> = MutableTransitionState(true),
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visibleState = visibleState) {
            Text(
                text = content ?: "",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight(800)),
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}