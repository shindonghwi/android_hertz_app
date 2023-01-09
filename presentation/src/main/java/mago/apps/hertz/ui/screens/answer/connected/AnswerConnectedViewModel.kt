package mago.apps.hertz.ui.screens.answer.connected

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
import javax.inject.Inject


@HiltViewModel
class AnswerConnectedViewModel @Inject constructor(
    private val getAnswerConnectedInfoUseCase: GetAnswerConnectedInfoUseCase
) : ViewModel() {

    private val _answerConnectedState = MutableStateFlow(AnswerConnectedState())
    val answerConnectedState: StateFlow<AnswerConnectedState> = _answerConnectedState

    suspend fun getAnswerConnectedInfo(answerSeq: Int) {
        getAnswerConnectedInfoUseCase(answerSeq).onEach {
            when (it) {
                is Resource.Loading -> {
                    _answerConnectedState.value = AnswerConnectedState(
                        isLoading = mutableStateOf(true),
                    )
                }
                is Resource.Error -> {
                    _answerConnectedState.value = AnswerConnectedState(
                        isLoading = mutableStateOf(false),
                        isErrorState = mutableStateOf(true),
                        error = it.message.toString()
                    )
                }
                is Resource.Success -> {
                    _answerConnectedState.value = AnswerConnectedState(
                        isLoading = mutableStateOf(false),
                        isSuccessState = mutableStateOf(true),
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}