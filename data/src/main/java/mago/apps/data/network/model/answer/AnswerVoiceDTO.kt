package mago.apps.data.network.model.answer

data class AnswerVoiceDTO(
    val text: String,
    val duration: Int,
    val voiceUrl: String,
    val waveformUrl: String
)