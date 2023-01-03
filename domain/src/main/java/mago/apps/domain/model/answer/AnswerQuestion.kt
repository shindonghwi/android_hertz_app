package mago.apps.domain.model.answer

data class AnswerQuestion(
    val questionSeq: Int,
    val text: String,
    val isLiked: Boolean
)