package mago.apps.hertz.ui.screens.answer.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
    likeDefaultState: Boolean? = null,
    iLikeActionCallback: ILikeActionCallback? = null
) {
    val likeState = remember { mutableStateOf(likeDefaultState ?: false)  }

    Row(
        modifier = modifier,
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
                        likeState.value= !likeState.value
                        iLikeActionCallback?.onState(likeState.value)
                    },
                imageVector = if (likeState.value) {
                    Icons.Default.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}