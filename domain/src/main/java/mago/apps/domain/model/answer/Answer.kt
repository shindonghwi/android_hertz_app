package mago.apps.domain.model.answer

data class Answer(
    val answerSeq: Int,
    val question: AnswerQuestion,
    val voice: AnswerVoice?,
    val tagList: List<String>,
    val shareType: String,
    val timeAgo: String,
    var timeAgoDisplay: String?, // 페이징에서 사용하는 정보
    val createdAt: String?
)



