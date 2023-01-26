package mago.apps.hertz.ui.screens.answer.register.text

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
import mago.apps.domain.model.common.EmotionType
import mago.apps.domain.model.common.Resource
import mago.apps.domain.model.question.QuestionRandom
import mago.apps.domain.usecases.question.PostAnswerTextUseCase
import mago.apps.hertz.ui.model.toast.TOAST_CODE_QUESTION_1
import mago.apps.hertz.ui.model.toast.TOAST_CODE_QUESTION_2
import mago.apps.hertz.ui.model.toast.TOAST_CODE_QUESTION_3
import mago.apps.hertz.ui.utils.scope.onDefault
import javax.inject.Inject

@HiltViewModel
class AnswerTextViewModel @Inject constructor(
    private val postAnswerTextUseCase: PostAnswerTextUseCase
) : ViewModel() {

    /** 질문 & 예시 */
    var questionInfo: QuestionRandom? = null
        private set

    fun updateQuestionInfo(question: QuestionRandom) {
        questionInfo = question
    }

    /** 질문에 대한 답변 */
    private var editingAnswerText: String = ""
    fun updateCurrentEditingText(text: String) {
        editingAnswerText = text
    }

    /** 감정 주파수 타입 */
    private var emotionType: EmotionType = EmotionType.HAPPINESS
    fun updateEmotion(type: EmotionType) {
        emotionType = type
    }

    private val _postAnswerTextState = MutableStateFlow(TextRegisterState())
    val postAnswerTextState: StateFlow<TextRegisterState> = _postAnswerTextState

    suspend fun postAnswerText(): String? {
        if (questionInfo?.questionSeq == null) return TOAST_CODE_QUESTION_1
        else if (editingAnswerText.isEmpty()) return TOAST_CODE_QUESTION_2
        else if (tagList.isEmpty()) return TOAST_CODE_QUESTION_3
        else {
            postAnswerTextUseCase(
                questionInfo?.questionSeq!!,
                editingAnswerText,
                emotionType,
                if (tagList.isEmpty()) null else TextUtils.join(",", tagList)
            ).onEach {
                when (it) {
                    is Resource.Loading -> {
                        _postAnswerTextState.value = TextRegisterState(
                            isLoading = mutableStateOf(true),
                        )
                    }
                    is Resource.Error -> {
                        _postAnswerTextState.value = TextRegisterState(
                            isLoading = mutableStateOf(false),
                            isErrorState = mutableStateOf(true),
                            error = it.message.toString()
                        )
                    }
                    is Resource.Success -> {
                        onDefault {
                            delay(300)
                            _postAnswerTextState.value = TextRegisterState(
                                isLoading = mutableStateOf(false),
                                isSuccessState = mutableStateOf(true)
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
            return null
        }
    }

    /** 태그 관련 UI */
    lateinit var screenScrollState: ScrollState
    lateinit var tagListScrollState: LazyListState
    var scrollToBottomAction: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var scrollToEndAction: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val tagList = mutableStateListOf<String>()

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