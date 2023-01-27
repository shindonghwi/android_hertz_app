package mago.apps.hertz.ui.screens.answer.register.audio

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mago.apps.domain.model.common.Resource
import mago.apps.domain.model.question.QuestionRandom
import mago.apps.domain.usecases.question.PostAnswerVoiceUseCase
import mago.apps.hertz.ui.utils.recorder.PcmRecorder
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class AnswerAudioViewModel @Inject constructor(
    private val postAnswerVoiceUseCase: PostAnswerVoiceUseCase
) : ViewModel() {

    /** 질문 & 예시 */
    var questionInfo: QuestionRandom? = null
        private set

    fun updateQuestionInfo(question: QuestionRandom) {
        questionInfo = question
    }

    val pcmRecorder = PcmRecorder()

    val audioFocusState = pcmRecorder.audioFocusAuth

    private val _postAnswerVoiceState = MutableStateFlow(VoiceRegisterState())
    val postAnswerVoiceState: StateFlow<VoiceRegisterState> = _postAnswerVoiceState

    suspend fun updateVoiceRegisterState(voiceRegisterState: VoiceRegisterState) {
        _postAnswerVoiceState.emit(voiceRegisterState)
    }

    suspend fun postAnswerVoice(questionSeq: Int, file: MultipartBody.Part) {
        postAnswerVoiceUseCase(questionSeq, file).onEach {
            when (it) {
                is Resource.Loading -> {
                    _postAnswerVoiceState.value = VoiceRegisterState(
                        isLoading = mutableStateOf(true)
                    )
                }
                is Resource.Error -> {
                    pcmRecorder.removeFileFromFilePath(listOf("wav", "zip"))
                    _postAnswerVoiceState.value = VoiceRegisterState(
                        isLoading = mutableStateOf(false),
                        isErrorState = mutableStateOf(true),
                        error = it.message.toString()
                    )
                }
                is Resource.Success -> {
                    pcmRecorder.removeFileFromFilePath(listOf("wav", "zip"))
                    _postAnswerVoiceState.value = VoiceRegisterState(
                        isLoading = mutableStateOf(false),
                        data = it.data,
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}