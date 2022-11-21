package mago.apps.hertz.ui.screens.components.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FillButton(
    modifier: Modifier = Modifier,
    content: String, // 버튼에 들어갈 내용
    isEnabled: Boolean, // 버튼 상태 ( enable or disable )
    onClick: () -> Unit // 클릭 로직
) {
    Button(
        onClick = { onClick() },
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
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.onPrimary,
        )
    }
}