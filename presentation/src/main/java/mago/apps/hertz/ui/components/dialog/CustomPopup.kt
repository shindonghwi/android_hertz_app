package mago.apps.hertz.ui.components.dialog

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.delay
import mago.apps.hertz.ui.components.dialog.permission.PopupPermission
import mago.apps.hertz.ui.components.dialog.record_end_frequency.PopupRecordEndFrequency
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnDefault


enum class PopupType {
    PERMISSION,
    FALLBACK,
    RECORD_END_FREQUENCY
}

interface PopupCallback {
    fun onState(isVisible: Boolean)
}

@Composable
fun CustomPopup(
    type: PopupType,
    fallbackMessage: String? = null,
    callback: PopupCallback
) {

    val isVisible = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        coroutineScopeOnDefault {
            delay(50)
            isVisible.value = true
            callback.onState(isVisible.value)
        }
    }

    Popup {
        AnimatedVisibility(
            visible = isVisible.value,
            enter = fadeIn(animationSpec = tween(durationMillis = 500)),
            exit = fadeOut(animationSpec = tween(durationMillis = 250)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .noDuplicationClickable {
                        PopupDisable(isVisible, callback)
                    },
                contentAlignment = Alignment.Center
            ) {
                when (type) {
                    PopupType.PERMISSION -> {
                        PopupPermission()
                    }
                    PopupType.FALLBACK -> {
                        PopupFallback(fallbackMessage)
                    }
                    PopupType.RECORD_END_FREQUENCY -> {
                        PopupRecordEndFrequency()
                    }
                }
            }
        }
    }

    BackHandler(enabled = isVisible.value) {
        PopupDisable(isVisible, callback)
    }
}

private fun PopupDisable(isVisible: MutableState<Boolean>, callback: PopupCallback) {
    isVisible.value = false
    coroutineScopeOnDefault {
        delay(300)
        callback.onState(false)
    }
}
