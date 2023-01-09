package mago.apps.data.mappers.answer

import mago.apps.data.network.model.answer.*
import mago.apps.domain.model.answer.*
import java.text.SimpleDateFormat
import java.util.*

fun AnswerDTO.toDomain(): Answer {
    return Answer(
        answerSeq = answerSeq,
        property = property?.toDomain(),
        question = question.toDomain(),
        common = common?.toDomain(),
        voiceList = voiceList?.map { it.toDomain() },
        voice = voice?.toDomain(),
        tagList = tagList,
        shareType = shareType,
        emotion = emotion,
        timeAgo = timeAgo,
        createdAt = createdAt.toDomainCreatedAt(),
        firstDayInList = null,
    )
}

fun AnswerCommonDTO.toDomain(): AnswerCommon {
    return AnswerCommon(emotion?.toDomain(), keywordList)
}

fun AnswerEmotionDTO.toDomain(): AnswerEmotion {
    return AnswerEmotion(type, rate)
}

fun AnswerPropertyDTO.toDomain(): AnswerProperty {
    return AnswerProperty(isSent, isConnected)
}

fun AnswerQuestionDTO.toDomain(): AnswerQuestion {
    return AnswerQuestion(questionSeq, text, isLiked)
}

fun AnswerVoiceDTO.toDomain(): AnswerVoice {
    return AnswerVoice(
        text,
        emotion,
        emotionList.map { it.toDomain() },
        duration,
        voiceUrl,
        waveformUrl,
        tagList
    )
}

fun AnswerEmotionListDTO.toDomain(): AnswerEmotion {
    return AnswerEmotion(type, rate)
}

fun String.toDomainCreatedAt(): String {
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