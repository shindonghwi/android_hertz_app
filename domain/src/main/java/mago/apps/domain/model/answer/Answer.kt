package mago.apps.domain.model.answer

data class Answer(
    val answerSeq: Int,
    val property: AnswerProperty?,
    val question: AnswerQuestion,
    val voice: AnswerVoice?,
    val tagList: List<String>,
    val shareType: String?,
    val emotion: String?,
    val timeAgo: String?,
    val createdAt: String,
    var firstDayInList: String?, // 페이징 목록중에 첫번째 날짜
)



