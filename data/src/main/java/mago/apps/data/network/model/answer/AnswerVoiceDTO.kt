package mago.apps.data.network.model.answer

data class AnswerVoiceDTO(
    val text: String,
    val emotion: String?,
    val emotionList: List<AnswerEmotionListDTO>,
    val duration: Float,
    val voiceUrl: String?,
    val waveformUrl: String?
)