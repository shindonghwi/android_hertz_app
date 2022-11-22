package mago.apps.hertz.ui.screens.components.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

interface ITextCallback {
    fun renderText(content: String)
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = "",
    textColor: Color = MaterialTheme.colorScheme.primary,
    textLimit: Int = Int.MAX_VALUE,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = Color.White,
        textAlign = TextAlign.Start
    ),
    placeHolderTextStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = Color.White.copy(alpha = 0.3f),
        textAlign = TextAlign.Start
    ),
    contentAlignment: Alignment = Alignment.TopStart,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
    singleLine: Boolean = true,
    textOverflow: TextOverflow = TextOverflow.Ellipsis,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    iTextCallback: ITextCallback? = null
) {
    var text by rememberSaveable { mutableStateOf("") }
    Box(contentAlignment = contentAlignment) {
        BasicTextField(
            modifier = modifier,
            value = text,
            onValueChange = { content ->

                content.takeIf {
                    content.length <= textLimit
                }?.apply {
                    text = content
                }
                iTextCallback?.renderText(text)
            },
            singleLine = singleLine,
            cursorBrush = cursorBrush,
            textStyle = textStyle,
            decorationBox = { innerTextField ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (leadingIcon != null) leadingIcon()
                    Box(
                        contentAlignment = contentAlignment
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = placeholderText,
                                style = placeHolderTextStyle,
                                color = textColor,
                                overflow = textOverflow,
                                maxLines = if (singleLine) 1 else Int.MAX_VALUE
                            )
                        }
                        innerTextField()
                    }
                    if (trailingIcon != null) trailingIcon()
                }
            },
            keyboardOptions = keyboardOptions ?: KeyboardOptions.Default,
            keyboardActions = keyboardActions ?: KeyboardActions.Default,
        )
    }
}