package mago.apps.hertz.ui.components.input

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

interface ITextCallback {
    fun renderText(content: String)
}

data class KeyBoardActionUnit(
    val onDone: (() -> Unit)? = null,
    val onGo: (() -> Unit)? = null,
    val onNext: (() -> Unit)? = null,
    val onPrevious: (() -> Unit)? = null,
    val onSearch: (() -> Unit)? = null,
    val onSend: (() -> Unit)? = null,
)

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    leadingPaddingValues: PaddingValues = PaddingValues(0.dp),
    trailingIcon: (@Composable () -> Unit)? = null,
    trailingPaddingValues: PaddingValues = PaddingValues(0.dp),
    innerTextPaddingValues: PaddingValues = PaddingValues(horizontal = 0.dp),
    placeholderText: (@Composable () -> Unit)? = null,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    isSingleLine: Boolean = true,
    textLimit: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    keyBoardActionUnit: KeyBoardActionUnit? = null,
    iTextCallback: ITextCallback? = null
) {
    var text by rememberSaveable { mutableStateOf("") }

    Box(modifier = modifier) {
        BasicTextField(value = text,
            onValueChange = {
                if (it.length <= textLimit) {
                    text = it
                    iTextCallback?.renderText(it)
                }
            },
            singleLine = isSingleLine,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onDone = { keyBoardActionUnit?.onDone?.let { it() } },
                onGo = { keyBoardActionUnit?.onGo?.let { it() } },
                onNext = { keyBoardActionUnit?.onNext?.let { it() } },
                onPrevious = { keyBoardActionUnit?.onPrevious?.let { it() } },
                onSearch = { keyBoardActionUnit?.onSearch?.let { it() } },
                onSend = { keyBoardActionUnit?.onSend?.let { it() } },
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.padding(leadingPaddingValues)) {
                            if (leadingIcon != null) leadingIcon()
                        }
                        Box(
                            Modifier
                                .weight(1f)
                                .padding(innerTextPaddingValues),
                        ) {
                            if (text.isEmpty()) {
                                placeholderText?.let { it() }
                            }
                            innerTextField()
                        }
                        Box(Modifier.padding(trailingPaddingValues)) {
                            if (trailingIcon != null) trailingIcon()
                        }
                    }
                }
            })
    }
}