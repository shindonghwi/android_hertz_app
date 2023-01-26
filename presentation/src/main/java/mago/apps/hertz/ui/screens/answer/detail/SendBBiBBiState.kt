package mago.apps.hertz.ui.screens.answer.detail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class SendBBiBBiState(
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val isErrorState : MutableState<Boolean> = mutableStateOf(false),
    val isSuccessState : MutableState<Boolean> = mutableStateOf(false),
    val error: String = ""
)
