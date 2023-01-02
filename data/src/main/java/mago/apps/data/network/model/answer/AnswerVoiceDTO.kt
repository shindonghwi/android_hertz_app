package mago.apps.data.network.model.answer

import mago.apps.domain.model.answer.AnswerEmotionList

data class AnswerVoiceDTO(
    val text: String,
    val emotionList: List<AnswerEmotionList>,
    val duration: Float,
    val voiceUrl: String,
    val waveformUrl: String
)