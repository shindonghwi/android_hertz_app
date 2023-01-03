package mago.apps.hertz.ui.screens.answer.detail

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.Resource
import mago.apps.domain.usecases.answer.GetAnswerInfoUseCase
import mago.apps.hertz.ui.utils.scope.onDefault
import javax.inject.Inject

@HiltViewModel
class AnswerDetailViewModel @Inject constructor(
    private val getAnswerInfoUseCase: GetAnswerInfoUseCase
) : ViewModel() {

    private val _answerState = MutableStateFlow(AnswerDetailState())
    val answerState: StateFlow<AnswerDetailState> = _answerState

    var isEditingMode: Boolean = false

    fun updateEditingMode(flag: Boolean) {
        isEditingMode = flag
    }

    /** 답변 정보 */
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
                    Log.w("asdasdasd", "Loading", )
                    _answerState.value = AnswerDetailState(
                        isLoading = mutableStateOf(true),
                    )
                }
                is Resource.Error -> {
                    Log.w("asdasdasd", "Error", )
                    _answerState.value = AnswerDetailState(
                        isLoading = mutableStateOf(false),
                        isErrorState = mutableStateOf(true),
                        error = it.message.toString()
                    )
                }
                is Resource.Success -> {
                    Log.w("asdasdasd", "Success: ${it.data}", )
                    _answerState.value = AnswerDetailState(
                        isLoading = mutableStateOf(false),
                        isSuccessState = mutableStateOf(true),
                        data = it.data
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}