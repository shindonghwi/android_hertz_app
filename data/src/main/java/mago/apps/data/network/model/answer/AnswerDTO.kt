package mago.apps.data.network.model.answer

import mago.apps.domain.model.answer.AnswerQuestion
import mago.apps.domain.model.answer.AnswerVoice

data class AnswerDTO(
    val answerSeq: Int,
    val question: AnswerQuestionDTO,
    val voice: AnswerVoiceDTO,
    val tagList: List<String>,
    val createdAt: String
)
