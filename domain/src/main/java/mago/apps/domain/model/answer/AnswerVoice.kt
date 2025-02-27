package mago.apps.domain.model.answer

data class AnswerVoice(
    val text: String,
    val emotion: String?,
    val emotionList: List<AnswerEmotion>,
    val duration: Float,
    val voiceUrl: String?,
    val waveformUrl: String?,
    val tagList: List<String>?
)