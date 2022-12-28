package mago.apps.data.mappers.answer

import mago.apps.data.network.model.answer.AnswerDTO
import mago.apps.data.network.model.answer.AnswerQuestionDTO
import mago.apps.data.network.model.answer.AnswerVoiceDTO
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.answer.AnswerQuestion
import mago.apps.domain.model.answer.AnswerVoice
import java.text.SimpleDateFormat
import java.util.*

fun AnswerDTO.toDomain(): Answer {
    return Answer(
        answerSeq,
        question.toDomain(),
        voice?.toDomain(),
        tagList,
        shareType,
        timeAgo.toDomainTimeAgo(),
        createdAt
    )
}

fun AnswerQuestionDTO.toDomain(): AnswerQuestion {
    return AnswerQuestion(text, isLiked)
}

fun AnswerVoiceDTO.toDomain(): AnswerVoice {
    return AnswerVoice(text, duration, voiceUrl, waveformUrl)
}

fun String.toDomainTimeAgo(): String {
    val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    val yearMonthDay = this.split(" ")
        .elementAtOrNull(0).toString() // 2022-12-12

    val parseIntDate = yearMonthDay.replace("-", "") // 20221212

    try {
        val calendar = Calendar.getInstance()
        dateFormat.parse(parseIntDate)?.let {
            calendar.time = it
            val yoilList = listOf("일", "월", "화", "수", "목", "금", "토")
            return "$yearMonthDay (${yoilList[calendar[Calendar.DAY_OF_WEEK] - 1]})"
        } ?: run {
            return ""
        }
    } catch (e: Exception) {
        return ""
    }
}