package mago.apps.hertz.ui.screens.question

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mago.apps.domain.model.common.Resource
import mago.apps.domain.usecases.question.GetQuestionRandomUseCase
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getQuestionRandomUseCase: GetQuestionRandomUseCase
) : ViewModel() {

    private val _questionVisible = MutableStateFlow(true)
    val questionVisible: StateFlow<Boolean> = _questionVisible

    private val _currentQuestion = MutableStateFlow("")
    val currentQuestion: StateFlow<String> = _currentQuestion

    var currentExample: String? = null

    suspend fun fetchQuestion() {
        getQuestionRandomUseCase.invoke().onEach {
            when (it) {
                is Resource.Error,
                is Resource.Loading -> {
                    _questionVisible.value = false
                }
                is Resource.Success -> {
                    _questionVisible.value = true
                    _currentQuestion.emit(it.data?.question.toString())
                    currentExample = it.data?.example.toString()
                }
            }
        }.launchIn(viewModelScope)
    }


}