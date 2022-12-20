package mago.apps.data.mappers.answer

import mago.apps.data.network.model.answer.AnswerDTO
import mago.apps.data.network.model.answer.AnswerQuestionDTO
import mago.apps.data.network.model.answer.AnswerVoiceDTO
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.answer.AnswerQuestion
import mago.apps.domain.model.answer.AnswerVoice

fun AnswerDTO.toDomain(): Answer {
    return Answer(answerSeq, question.toDomain(), voice.toDomain(), tagList, createdAt)
}

fun AnswerQuestionDTO.toDomain(): AnswerQuestion {
    return AnswerQuestion(text, isLiked)
}

fun AnswerVoiceDTO.toDomain(): AnswerVoice {
    return AnswerVoice(text, duration, voiceUrl, waveformUrl)
}