package mago.apps.hertz.ui.screens.answer.detail

import android.media.MediaPlayer
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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
                    updateTagList(it.data?.tagList)
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
    var mediaPlayer: MediaPlayer? = null
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying

    private fun updatePlayingState(flag: Boolean) {
        _isPlaying.value = flag
    }

    fun initPlayer() {
        mediaPlayer = MediaPlayer()
    }

    fun audioReset() {
        mediaPlayer?.run {
            pause()
            seekTo(0)
            updatePlayingState(false)
        }
    }

    fun audioStart() {
        mediaPlayer?.start()
        updatePlayingState(true)
    }

    fun audioClear() {
        mediaPlayer?.run {
            stop()
            release()
            null
        }
    }

    private val countUpTimer = CountUpTimer()
    fun getTime(duration: Int?) = countUpTimer.timeToString(duration ?: 0)

    /** 태그 관련 UI */
    lateinit var screenScrollState: ScrollState
    lateinit var tagListScrollState: LazyListState
    var scrollToBottomAction: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var scrollToEndAction: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val tagList = mutableStateListOf<String>()

    fun updateTagList(items: List<String>?) {
        items?.let { tagList.addAll(it) }
    }

    fun addTag(tag: String) {
        if (tag.isNotEmpty() && !tagList.contains(tag)) {
            tagList.add(tag)
        }
    }

    fun removeTag(tag: String) {
        tagList.remove(tag)
    }

    fun updateScrollBottomAction(flag: Boolean) {
        scrollToBottomAction.update { flag }
    }

    fun updateScrollEndAction(flag: Boolean) {
        scrollToEndAction.update { flag }
    }

    suspend fun scrollToBottom() {
        screenScrollState.animateScrollTo(screenScrollState.maxValue)
        delay(10)
        updateScrollBottomAction(false)
    }

    suspend fun scrollToEnd() {
        tagListScrollState.animateScrollToItem(tagList.lastIndex)
        delay(10)
        updateScrollEndAction(false)
    }
}