package mago.apps.hertz.ui.screens.answer.audio

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import mago.apps.hertz.ui.utils.permission.PermissionsHandler
import javax.inject.Inject

@OptIn(ExperimentalPermissionsApi::class)
@HiltViewModel
class AnswerAudioViewModel @Inject constructor() : ViewModel() {

    val isPlaying: MutableState<Boolean> = mutableStateOf(true)
    val isFrequencyPopUpVisible: MutableState<Boolean> = mutableStateOf(false)

    fun updatePlayingState(flag: Boolean) {
        isPlaying.value = flag
    }

    fun updatePopupState(flag: Boolean) {
        isFrequencyPopUpVisible.value = flag
    }

    val permissionsHandler: PermissionsHandler = PermissionsHandler()
    private val _state = MutableStateFlow(PermissionsHandler.State())
    val state: StateFlow<PermissionsHandler.State> = _state

    init {
        permissionsHandler
            .state
            .onEach { handlerState ->
                _state.update { it.copy(multiplePermissionsState = handlerState.multiplePermissionsState) }
            }
            .catch { Log.e("asdasdasd", "$it") }
            .launchIn(viewModelScope)
    }

}