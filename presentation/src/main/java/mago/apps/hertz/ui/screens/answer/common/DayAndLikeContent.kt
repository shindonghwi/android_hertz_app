package mago.apps.hertz.ui.screens.answer.common

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import java.text.SimpleDateFormat
import java.util.*

interface ILikeActionCallback {
    fun onState(likeState: Boolean)
}

@Composable
fun DayAndLikeContent(
    modifier: Modifier,
    timeText: String? = null,
    visibleState: MutableTransitionState<Boolean> = MutableTransitionState(true),
    likeDefaultState: Boolean? = null,
    iLikeActionCallback: ILikeActionCallback? = null
) {
    val likeState = remember { mutableStateOf(likeDefaultState ?: false) }

    Box(modifier = modifier) {
        AnimatedVisibility(visibleState = visibleState) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = timeText ?: SimpleDateFormat(
                        "yyyy년 MM월 dd일 EE요일", Locale.getDefault()
                    ).format(Calendar.getInstance().time),
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.titleMedium
                )

                likeDefaultState?.let {
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .noDuplicationClickable {
                                likeState.value = !likeState.value
                                iLikeActionCallback?.onState(likeState.value)
                            },
                        imageVector = if (likeState.value) {
                            Icons.Filled.ThumbUp
                        } else {
                            Icons.Outlined.ThumbUp
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}