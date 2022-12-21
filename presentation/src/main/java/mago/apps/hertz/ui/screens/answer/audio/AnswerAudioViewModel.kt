package mago.apps.hertz.ui.screens.answer.audio

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import mago.apps.hertz.ui.utils.permission.PermissionsHandler
import mago.apps.hertz.ui.utils.recorder.PcmRecorder
import mago.apps.hertz.ui.utils.scope.onDefault
import javax.inject.Inject

@OptIn(ExperimentalPermissionsApi::class)
@HiltViewModel
class AnswerAudioViewModel @Inject constructor() : ViewModel() {

    /** 질문 & 예시 */
    var question: String? = null
        private set
    var example: String? = null
        private set

    fun updateQuestionInfo(question: String?, example: String?) {
        this.question = question
        this.example = example
    }

    val pcmRecorder = PcmRecorder()
    val isPlaying: MutableState<Boolean> = mutableStateOf(true)
    val isFrequencyPopUpVisible: MutableState<Boolean> = mutableStateOf(false)

    fun updatePlayingState(flag: Boolean) {
        isPlaying.value = flag
    }

    fun updatePopupState(flag: Boolean) {
        isFrequencyPopUpVisible.value = flag
    }

    private val _state = MutableStateFlow(PermissionsHandler.State())
    val state: StateFlow<PermissionsHandler.State> = _state

    private fun onPermissionHandlerState() = onDefault {
        PermissionsHandler().state.onEach { handlerState ->
            _state.update { it.copy(multiplePermissionsState = handlerState.multiplePermissionsState) }
        }.catch { Log.e("permissionsHandler", "$it") }
    }

    init {
        onPermissionHandlerState()
    }

}