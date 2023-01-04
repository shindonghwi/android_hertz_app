package mago.apps.hertz.ui.screens.answer.detail.model

data class AnswerPatchData(
    var answerSeq: Int,
    var text: String,
    var tags: String,
    var anger: Int = 0,
    var neutral: Int = 0,
    var happiness: Int = 0,
    var sadness: Int = 0,
)
