package mago.apps.domain.model.answer

data class AnswerVoice(
    val text: String,
    val duration: Int,
    val voiceUrl: String,
    val waveformUrl: String
)