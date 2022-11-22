package mago.apps.hertz.ui.screens.components.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onClick: () -> Unit // 클릭 로직
) {
    ElevatedButton(
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
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        contentPadding = PaddingValues(all = 0.dp)
    ) {
        Text(
            content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}