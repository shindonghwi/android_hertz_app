package mago.apps.data.mappers.question

import mago.apps.data.network.model.question.QuestionRandomDTO
import mago.apps.domain.model.question.QuestionRandom

fun QuestionRandomDTO.toDomain(): QuestionRandom {
    return QuestionRandom(question, example)
}