package mago.apps.data.network.model.answer

import mago.apps.domain.model.answer.AnswerCommon

data class AnswerDTO(
    val answerSeq: Int,
    val property: AnswerPropertyDTO?,
    val question: AnswerQuestionDTO,
    val common: AnswerCommonDTO,
    val voice: AnswerVoiceDTO?,
    val tagList: List<String>,
    val shareType: String?,
    val emotion: String?,
    val timeAgo: String?,
    val createdAt: String,
    var firstDayInList: String?, // 페이징 목록중에 첫번째 날짜
)
