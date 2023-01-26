package mago.apps.data.mappers.question

import mago.apps.data.network.model.question.QuestionPropertyDTO
import mago.apps.data.network.model.question.QuestionRandomDTO
import mago.apps.domain.model.question.QuestionProperty
import mago.apps.domain.model.question.QuestionRandom

fun QuestionRandomDTO.toDomain(): QuestionRandom {
    return QuestionRandom(questionSeq, property?.toDomain(), text, example)
}

fun QuestionPropertyDTO.toDomain(): QuestionProperty {
    return QuestionProperty(name)
}