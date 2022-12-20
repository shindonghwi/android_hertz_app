package mago.apps.domain.model.answer

data class Answer(
    val answerSeq: Int,
    val question: AnswerQuestion,
    val voice: AnswerVoice,
    val tagList: List<String>,
    val createdAt: String
)



