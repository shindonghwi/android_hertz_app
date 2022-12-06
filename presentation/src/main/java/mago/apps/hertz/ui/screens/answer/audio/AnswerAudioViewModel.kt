package mago.apps.hertz.ui.screens.answer.audio

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AnswerAudioViewModel @Inject constructor(): ViewModel() {

    val isPlaying: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isShowingPopup: MutableStateFlow<Boolean> = MutableStateFlow(false)

    suspend fun updatePlayingState(flag: Boolean){
        isPlaying.emit(flag)
    }

    suspend fun updatePopupState(flag: Boolean){
        isShowingPopup.emit(flag)
    }
}