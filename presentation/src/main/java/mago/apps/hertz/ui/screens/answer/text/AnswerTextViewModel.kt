package mago.apps.hertz.ui.screens.answer.text

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.question.QuestionRandom
import javax.inject.Inject

@HiltViewModel
class AnswerTextViewModel @Inject constructor() : ViewModel() {

    /** 질문 & 예시 */
    var questionInfo: QuestionRandom? = null
        private set

    fun updateQuestionInfo(question: QuestionRandom) {
        questionInfo = question
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