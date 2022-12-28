package mago.apps.hertz.ui.screens.episode_list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.DataListType

data class EpisodeListState(
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val isErrorState : MutableState<Boolean> = mutableStateOf(false),
    val isSuccessState : MutableState<Boolean> = mutableStateOf(false),
    var data: DataListType<Answer>? = null,
    val error: String = ""
)
