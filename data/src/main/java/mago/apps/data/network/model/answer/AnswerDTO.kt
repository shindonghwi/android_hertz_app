package mago.apps.data.network.model.answer

data class AnswerDTO(
    val answerSeq: Int,
    val question: AnswerQuestionDTO,
    val voice: AnswerVoiceDTO?,
    val tagList: List<String>,
    val shareType: String,
    val emotion: String,
    val timeAgo: String,
    val createdAt: String
)
