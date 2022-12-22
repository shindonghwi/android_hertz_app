package mago.apps.hertz.ui.screens.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mago.apps.domain.model.common.Resource
import mago.apps.domain.model.question.QuestionRandom
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

    var questionInfo: QuestionRandom? = null

    suspend fun fetchQuestion() {
        getQuestionRandomUseCase.invoke().onEach {
            when (it) {
                is Resource.Error,
                is Resource.Loading -> {
                    _questionVisible.value = false
                }
                is Resource.Success -> {
                    _questionVisible.value = true
                    _currentQuestion.emit(it.data?.text.toString())
                    questionInfo = it.data
                }
            }
        }.launchIn(viewModelScope)
    }


}