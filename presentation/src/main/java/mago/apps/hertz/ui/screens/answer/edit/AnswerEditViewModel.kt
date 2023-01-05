package mago.apps.hertz.ui.screens.answer.edit

import android.text.TextUtils
import android.util.Log
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
import mago.apps.domain.model.answer.AnswerEmotion
import mago.apps.domain.model.common.EmotionList
import mago.apps.domain.model.common.EmotionType
import mago.apps.domain.model.common.Resource
import mago.apps.domain.usecases.answer.PatchAnswerUseCase
import mago.apps.hertz.ui.model.toast.TOAST_CODE_QUESTION_1
import mago.apps.hertz.ui.model.toast.TOAST_CODE_QUESTION_2
import mago.apps.hertz.ui.model.toast.TOAST_CODE_QUESTION_3
import mago.apps.hertz.ui.screens.answer.detail.AnswerPatchState
import mago.apps.hertz.ui.screens.answer.edit.model.AnswerPatchData
import mago.apps.hertz.ui.screens.answer.edit.model.FrequencyData
import javax.inject.Inject

@HiltViewModel
class AnswerEditViewModel @Inject constructor(
    private val patchAnswerUseCase: PatchAnswerUseCase
) : ViewModel() {

    /** 답변 정보 초기화 */
    var answerData: Answer? = null

    fun initAnswerData(answer: Answer?) {
        answerData = answer
        updateTagList(answerData?.tagList)
        updateEmotionList(answerData?.voice?.emotionList)
        initPathAnswerData(answerData)
    }

    /** 답변 수정 데이터 관리 */
    var answerPatchData: AnswerPatchData? = null
    private fun initPathAnswerData(data: Answer?) {
        answerPatchData = AnswerPatchData(
            answerSeq = data?.answerSeq ?: 0,
            text = data?.voice?.text.toString(),
            tags = data?.tagList?.let { TextUtils.join(",", it) } ?: "",
            angry = data?.voice?.emotionList?.filter { it.type == EmotionType.ANGRY.name }
                ?.elementAtOrNull(0)?.rate ?: 0,
            neutral = data?.voice?.emotionList?.filter { it.type == EmotionType.NEUTRAL.name }
                ?.elementAtOrNull(0)?.rate ?: 0,
            happiness = data?.voice?.emotionList?.filter { it.type == EmotionType.HAPPINESS.name }
                ?.elementAtOrNull(0)?.rate ?: 0,
            sadness = data?.voice?.emotionList?.filter { it.type == EmotionType.SADNESS.name }
                ?.elementAtOrNull(0)?.rate ?: 0,
        )
    }

    fun updatePatchAnswerText(text: String) {
        answerPatchData?.text = text
    }

    private fun updatePatchAnswerTag(tags: List<String>) {
        answerPatchData?.tags = TextUtils.join(",", tags)
    }

    fun updatePatchAnswerEmotion(type: String, rate: Int) {
        when (type) {
            EmotionType.HAPPINESS.name -> answerPatchData?.happiness = rate
            EmotionType.SADNESS.name -> answerPatchData?.sadness = rate
            EmotionType.ANGRY.name -> answerPatchData?.angry = rate
            EmotionType.NEUTRAL.name -> answerPatchData?.neutral = rate
        }
    }

    /** 답변 수정하기 API */
    private val _answerPatchState = MutableStateFlow(AnswerPatchState())
    val answerPatchState: StateFlow<AnswerPatchState> = _answerPatchState

    suspend fun patchAnswerData(): String? {
        answerPatchData?.let {
            if (it.answerSeq == 0) return TOAST_CODE_QUESTION_1
            else if (it.text.isEmpty()) return TOAST_CODE_QUESTION_2
            else if (it.tags.isEmpty()) return TOAST_CODE_QUESTION_3
            else {
                patchAnswerUseCase(
                    it.answerSeq, it.text, it.tags,
                    it.angry, it.neutral, it.happiness, it.sadness
                ).onEach {
                    when (it) {
                        is Resource.Loading -> {
                            _answerPatchState.value = AnswerPatchState(
                                isLoading = mutableStateOf(true),
                            )
                        }
                        is Resource.Error -> {
                            _answerPatchState.value = AnswerPatchState(
                                isLoading = mutableStateOf(false),
                                isErrorState = mutableStateOf(true),
                                error = it.message.toString()
                            )
                        }
                        is Resource.Success -> {
                            _answerPatchState.value = AnswerPatchState(
                                isLoading = mutableStateOf(false),
                                isSuccessState = mutableStateOf(true),
                                data = it.data
                            )
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
        return null
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
    private fun updateEmotionList(emotionList: List<AnswerEmotion>?) {
        if (frequencyInfoList.isEmpty()) {
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
    }


    /** 태그 관련 UI */
    lateinit var screenScrollState: ScrollState
    lateinit var tagListScrollState: LazyListState
    var scrollToBottomAction: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var scrollToEndAction: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val tagList = mutableStateListOf<String>()

    private fun updateTagList(items: List<String>?) {
        if (tagList.isEmpty()) {
            items?.let { tagList.addAll(it) }
        }
    }

    fun addTag(tag: String) {
        if (tag.isNotEmpty() && !tagList.contains(tag)) {
            tagList.add(tag)
            updatePatchAnswerTag(tagList)
        }
    }

    fun removeTag(tag: String) {
        tagList.remove(tag)
        updatePatchAnswerTag(tagList)
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