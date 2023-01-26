package mago.apps.data.network.model.answer

data class AnswerQuestionDTO(
    val questionSeq: Int,
    val text: String,
    val isLiked: Boolean
)