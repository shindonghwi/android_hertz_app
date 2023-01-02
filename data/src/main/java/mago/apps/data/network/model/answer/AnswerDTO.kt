package mago.apps.data.network.model.answer

import mago.apps.domain.model.answer.AnswerProperty

data class AnswerDTO(
    val answerSeq: Int,
    val property: AnswerProperty?,
    val question: AnswerQuestionDTO,
    val voice: AnswerVoiceDTO?,
    val tagList: List<String>,
    val shareType: String?,
    val emotion: String?,
    val timeAgo: String?,
    val createdAt: String,
    var firstDayInList: String?, // 페이징 목록중에 첫번째 날짜

)
