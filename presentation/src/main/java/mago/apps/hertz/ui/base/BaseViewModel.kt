package mago.apps.hertz.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mago.apps.hertz.ui.model.state.UiState

open class BaseViewModel<T> : ViewModel() {

    val _uiState = MutableStateFlow<UiState<T>>(UiState.Idle)
    val uiState: StateFlow<UiState<T>> = _uiState

}