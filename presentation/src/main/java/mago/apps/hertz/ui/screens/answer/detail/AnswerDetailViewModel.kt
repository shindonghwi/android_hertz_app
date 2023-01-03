package mago.apps.hertz.ui.screens.answer.detail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.question.QuestionRandom
import javax.inject.Inject

@HiltViewModel
class AnswerDetailViewModel @Inject constructor(
) : ViewModel() {

    /** 답변 정보 */
    var answerInfo: Answer? = null
        private set

    fun updateAnswerInfo(answer: Answer) {
        answerInfo = answer
    }

}