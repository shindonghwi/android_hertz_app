package mago.apps.data.network.model.question

data class QuestionRandomDTO(
    val questionSeq: Int,
    val property: QuestionPropertyDTO?,
    val text: String,
    val example: String
)
