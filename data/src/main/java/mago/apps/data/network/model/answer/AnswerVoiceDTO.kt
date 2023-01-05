package mago.apps.data.network.model.answer

import mago.apps.domain.model.answer.AnswerEmotion

data class AnswerVoiceDTO(
    val text: String,
    val emotionList: List<AnswerEmotion>,
    val duration: Float,
    val voiceUrl: String?,
    val waveformUrl: String?
)