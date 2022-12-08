package mago.apps.hertz.ui.screens.answer.text

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AnswerTextViewModel @Inject constructor() : ViewModel() {

    lateinit var screenScrollState: ScrollState
    lateinit var tagListScrollState: LazyListState
    var scrollToBottomAction: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var scrollToEndAction: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val isFrequencyExistState = mutableStateOf(false)

    fun updateFrequencyExistState(isExist: Boolean) {
        isFrequencyExistState.value = isExist
    }

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