package mago.apps.hertz.ui.screens.answer.detail

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.answer.AnswerEmotionList
import mago.apps.domain.model.common.EmotionList
import mago.apps.domain.model.common.Resource
import mago.apps.domain.usecases.answer.GetAnswerInfoUseCase
import mago.apps.domain.usecases.question.DelLikeUseCase
import mago.apps.domain.usecases.question.PostLikeUseCase
import mago.apps.domain.usecases.question.PostSendQuestionFriendUseCase
import mago.apps.hertz.ui.screens.answer.detail.model.FrequencyData
import mago.apps.hertz.ui.utils.recorder.CountUpTimer
import javax.inject.Inject



@HiltViewModel
class AnswerDetailViewModel @Inject constructor(
    private val getAnswerInfoUseCase: GetAnswerInfoUseCase,
    private val postLikeUseCase: PostLikeUseCase,
    private val delLikeUseCase: DelLikeUseCase,
    private val postSendQuestionFriendUseCase: PostSendQuestionFriendUseCase
) : ViewModel() {


    /** 수정모드 */
    private var _isEditingMode = MutableStateFlow(false)
    var isEditingMode = _isEditingMode

    fun updateEditingMode(flag: Boolean) {
        _isEditingMode.value = flag
    }

    /** 감정 주파수 데이터 */
    var selectedFrequencyIndex = MutableStateFlow(0)

    fun updateSelectedFrequencyIndex(index: Int) {
        selectedFrequencyIndex.value = index
    }

    fun updateFrequencyScore(score: String) {
        frequencyInfoList[selectedFrequencyIndex.value].rate.value = score
    }

    var frequencyInfoList = arrayListOf<FrequencyData>()

    // 감정 주파수 데이터 초기 셋팅
    private fun updateEmotionList(emotionList: List<AnswerEmotionList>?) {
        emotionList?.let {
            repeat(it.size) { idx ->
                val type = it[idx].type
                val icon = EmotionList.find { type == it.second.name }?.first.toString()
                val iconType = EmotionList.find { type == it.second.name }?.second.toString()
                val rate = mutableStateOf("${it.find { it.type == iconType }?.rate}")
                frequencyInfoList.add(FrequencyData(icon, iconType, rate, rate.value))
            }
        }
    }



    /** 답변 정보 */
    private val _answerState = MutableStateFlow(AnswerDetailState())
    val answerState: StateFlow<AnswerDetailState> = _answerState
    fun updateAnswerState(answer: Answer) {
        _answerState.value = AnswerDetailState(
            isLoading = mutableStateOf(false),
            isSuccessState = mutableStateOf(true),
            data = answer
        )
    }

    suspend fun getAnswerInfo(answerSeq: Int) {
        getAnswerInfoUseCase(answerSeq).onEach {
            when (it) {
                is Resource.Loading -> {
                    _answerState.value = AnswerDetailState(
                        isLoading = mutableStateOf(true),
                    )
                }
                is Resource.Error -> {
                    _answerState.value = AnswerDetailState(
                        isLoading = mutableStateOf(false),
                        isErrorState = mutableStateOf(true),
                        error = it.message.toString()
                    )
                }
                is Resource.Success -> {
                    updateEmotionList(it.data?.voice?.emotionList)
                    _answerState.value = AnswerDetailState(
                        isLoading = mutableStateOf(false),
                        isSuccessState = mutableStateOf(true),
                        data = it.data
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    /** 삐삐 전송 */
    private val _bbibbiState = MutableStateFlow(SendBBiBBiState())
    val bbibbiState: StateFlow<SendBBiBBiState> = _bbibbiState

    suspend fun postSendQuestionFriend(questionSeq: Int) {
        postSendQuestionFriendUseCase(questionSeq).onEach {
            when (it) {
                is Resource.Loading -> {
                    _bbibbiState.value = SendBBiBBiState(
                        isLoading = mutableStateOf(true),
                    )
                }
                is Resource.Error -> {
                    _bbibbiState.value = SendBBiBBiState(
                        isLoading = mutableStateOf(false),
                        isErrorState = mutableStateOf(true),
                        error = it.message.toString()
                    )
                }
                is Resource.Success -> {
                    _bbibbiState.value = SendBBiBBiState(
                        isLoading = mutableStateOf(false),
                        isSuccessState = mutableStateOf(true),
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    /** 좋아요, 좋아요 취소*/
    suspend fun postLike(questionSeq: Int) = postLikeUseCase(questionSeq).launchIn(viewModelScope)
    suspend fun delLike(questionSeq: Int) = delLikeUseCase(questionSeq).launchIn(viewModelScope)

    /** 음성 플레이, 정지 */
    val mediaPlayer = MediaPlayer()
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying

    fun updatePlayingState(flag: Boolean) {
        _isPlaying.value = flag
    }

    fun audioReset() {
        mediaPlayer.run {
            pause()
            seekTo(0)
            updatePlayingState(false)
        }
    }

    fun audioStart() {
        mediaPlayer.start()
        updatePlayingState(true)
    }

    private val countUpTimer = CountUpTimer()
    fun getTime(duration: Int?) = countUpTimer.timeToString(duration ?: 0)
}