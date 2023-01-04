package mago.apps.hertz.ui.screens.answer.detail.model

import androidx.compose.runtime.MutableState

data class FrequencyData(
    val icon: String,
    val iconType: String,
    val rate: MutableState<String>,
    val initRate: String
)