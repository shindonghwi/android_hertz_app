package mago.apps.domain.model.answer

data class AnswerCommon(
    val emotion: AnswerEmotion,
    val keywordList: List<String>
)