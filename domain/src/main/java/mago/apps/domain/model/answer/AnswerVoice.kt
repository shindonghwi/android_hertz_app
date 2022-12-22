package mago.apps.domain.model.answer

data class AnswerVoice(
    val text: String,
    val duration: Float,
    val voiceUrl: String,
    val waveformUrl: String
)