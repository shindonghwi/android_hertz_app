package mago.apps.hertz.ui.screens.answer.text

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import mago.apps.domain.model.answer.Answer

data class TextRegisterState(
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val isErrorState : MutableState<Boolean> = mutableStateOf(false),
    val isSuccessState : MutableState<Boolean> = mutableStateOf(false),
    var data: Answer? = null,
    val error: String = ""
)
