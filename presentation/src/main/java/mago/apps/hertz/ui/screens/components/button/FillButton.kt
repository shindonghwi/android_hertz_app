package mago.apps.hertz.ui.screens.components.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mago.apps.hertz.ui.utils.compose.modifier.ClickableCheckObject

@Composable
fun FillButton(
    modifier: Modifier = Modifier,
    content: String, // 버튼에 들어갈 내용
    isEnabled: Boolean, // 버튼 상태 ( enable or disable )
    onClick: () -> Unit // 클릭 로직
) {
    Button(
        onClick = {
            ClickableCheckObject.throttledFirstClicks {
                onClick.invoke()
            }
        },
        shape = RoundedCornerShape(16.dp),
        elevation = null,
        enabled = isEnabled,
//        interactionSource = interactionSource,
//        colors = ButtonDefaults.buttonColors(
//            disabledBackgroundColor = getButtonDisableColor(buttonComponent.style),
//            contentColor = getButtonColor(isPressed = true, style = buttonComponent.style),
//            backgroundColor = getButtonColor(isPressed, buttonComponent.style)
//        ),
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