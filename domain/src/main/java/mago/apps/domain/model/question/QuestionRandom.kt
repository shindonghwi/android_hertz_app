package mago.apps.domain.model.question

data class QuestionRandom(
    val questionSeq: Int,
    val property: QuestionProperty?,
    val text: String,
    val example: String
)