package mago.apps.hertz.ui.components.dialog

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import mago.apps.hertz.ui.components.dialog.fallback.PopupFallback
import mago.apps.hertz.ui.components.dialog.permission.PopupPermission
import mago.apps.hertz.ui.components.dialog.record_end_frequency.PopupRecordEndFrequency
import mago.apps.hertz.ui.components.dialog.register.PopupRegister
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable


enum class PopupType {
    PERMISSION,
    FALLBACK,
    RECORD_END_FREQUENCY,
    REGISTER
}

interface PopupPermissionCallback {
    fun allAllow()
    fun deny()
}

interface IBackPressEvent {
    fun onPress()
}

@Composable
fun CustomPopup(
    isVisible: MutableState<Boolean>,
    backgroundTouchEnable: Boolean = true,
    type: PopupType,
    showingMessage: String? = null,
    iBackPressEvent: IBackPressEvent? = null,
    permissionCallback: PopupPermissionCallback? = null,
) {
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
                    .noDuplicationClickable(backgroundTouchEnable) {
                        iBackPressEvent?.onPress()
                        popupDisable(isVisible)
                    },
                contentAlignment = Alignment.Center
            ) {
                val configuration = LocalConfiguration.current
                val size = configuration.screenWidthDp.dp * 0.85f

                val popupModifier = Modifier
                    .size(size)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {})
                    .padding(
                        if (type == PopupType.PERMISSION) {
                            PaddingValues(top = 20.dp)
                        } else{
                            PaddingValues(all = 20.dp)
                        }
                    )

                when (type) {
                    PopupType.PERMISSION -> {
                        PopupPermission(
                            modifier = popupModifier,
                            permissionCallback = permissionCallback
                        )
                    }
                    PopupType.FALLBACK -> {
                        PopupFallback(modifier = popupModifier, showingMessage = showingMessage)
                    }
                    PopupType.REGISTER -> {
                        PopupRegister(modifier = popupModifier, showingMessage = showingMessage)
                    }
                    PopupType.RECORD_END_FREQUENCY -> {
                        PopupRecordEndFrequency(modifier = popupModifier)
                    }
                }
            }
        }
    }

    BackHandler(enabled = isVisible.value) {
        if (backgroundTouchEnable) {
            iBackPressEvent?.onPress()
            popupDisable(isVisible)
        }
    }
}

private fun popupDisable(isVisible: MutableState<Boolean>) {
    isVisible.value = false
}
