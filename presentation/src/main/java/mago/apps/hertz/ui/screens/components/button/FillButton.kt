package mago.apps.hertz.ui.screens.components.button

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mago.apps.hertz.ui.utils.compose.modifier.ClickableCheckObject

data class ButtonColor(
    val disable: Color,
    val content: Color,
    val disabledContainer: Color,
    val container: Color
)

@Composable
fun FillButton(
    modifier: Modifier = Modifier,
    content: String, // 버튼에 들어갈 내용
    isEnabled: Boolean, // 버튼 상태 ( enable or disable )
    buttonColor: ButtonColor = ButtonColor(
        disable = MaterialTheme.colorScheme.onSurfaceVariant,
        content = MaterialTheme.colorScheme.onPrimary,
        disabledContainer = MaterialTheme.colorScheme.onSurfaceVariant.copy(
            alpha = 0.8f
        ),
        container = MaterialTheme.colorScheme.primary,
    ),
    tailIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit // 클릭 로직
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ElevatedButton(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    ClickableCheckObject.throttledFirstClicks {
                        onClick.invoke()
                    }
                },
                shape = RoundedCornerShape(16.dp),
                elevation = null,
                enabled = isEnabled,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = buttonColor.disabledContainer,
                    contentColor = buttonColor.content,
                    disabledContentColor = buttonColor.disabledContainer,
                    containerColor = buttonColor.container
                ),
                contentPadding = PaddingValues(all = 0.dp)
            ) {
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
            tailIcon?.let { it() }
        }
    }

}