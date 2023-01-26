package mago.apps.data.network.model.answer

data class AnswerDTO(
    val answerSeq: Int,
    val property: AnswerPropertyDTO?,
    val question: AnswerQuestionDTO,
    val common: AnswerCommonDTO?,
    val voiceList: List<AnswerVoiceDTO>?,
    val voice: AnswerVoiceDTO?,
    val tagList: List<String>?,
    val shareType: String?,
    val emotion: String?,
    val timeAgo: String?,
    val createdAt: String,
    var firstDayInList: String?, // 페이징 목록중에 첫번째 날짜
)
