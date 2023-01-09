package mago.apps.hertz.ui.screens.answer.connected

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mago.apps.domain.model.common.Resource
import mago.apps.domain.usecases.answer.GetAnswerConnectedInfoUseCase
import mago.apps.domain.usecases.question.DelLikeUseCase
import mago.apps.domain.usecases.question.PostLikeUseCase
import javax.inject.Inject


@HiltViewModel
class AnswerConnectedViewModel @Inject constructor(
    private val getAnswerConnectedInfoUseCase: GetAnswerConnectedInfoUseCase,
    private val postLikeUseCase: PostLikeUseCase,
    private val delLikeUseCase: DelLikeUseCase,
) : ViewModel() {

    private val _answerConnectedState = MutableStateFlow(AnswerConnectedState())
    val answerConnectedState: StateFlow<AnswerConnectedState> = _answerConnectedState

    suspend fun getAnswerConnectedInfo(answerSeq: Int) {
        getAnswerConnectedInfoUseCase(answerSeq).onEach {
            when (it) {
                is Resource.Loading -> {
                    Log.w("ASdassd", "getAnswerConnectedInfo: loading")
                    _answerConnectedState.value = AnswerConnectedState(
                        isLoading = mutableStateOf(true),
                    )
                }
                is Resource.Error -> {
                    Log.w("ASdassd", "getAnswerConnectedInfo: error: ${it.message}")
                    _answerConnectedState.value = AnswerConnectedState(
                        isLoading = mutableStateOf(false),
                        isErrorState = mutableStateOf(true),
                        error = it.message.toString()
                    )
                }
                is Resource.Success -> {
                    Log.w("ASdassd", "getAnswerConnectedInfo: success: ${it.data}")
                    _answerConnectedState.value = AnswerConnectedState(
                        isLoading = mutableStateOf(false),
                        isSuccessState = mutableStateOf(true),
                        data = it.data
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    /** 좋아요, 좋아요 취소*/
    suspend fun postLike(questionSeq: Int) = postLikeUseCase(questionSeq).launchIn(viewModelScope)
    suspend fun delLike(questionSeq: Int) = delLikeUseCase(questionSeq).launchIn(viewModelScope)

}