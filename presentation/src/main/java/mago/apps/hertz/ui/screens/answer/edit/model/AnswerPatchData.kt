package mago.apps.hertz.ui.screens.answer.edit.model

import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.answer.AnswerEmotion
import mago.apps.domain.model.answer.AnswerVoice
import mago.apps.domain.model.common.EmotionType

data class AnswerPatchData(
    var answerSeq: Int,
    var emotion: String?,
    var text: String,
    var tags: String,
    var angry: Int = 0,
    var neutral: Int = 0,
    var happiness: Int = 0,
    var sadness: Int = 0,
)

fun AnswerPatchData.toAnswerData(
    defaultAnswerData: Answer
): Answer {

    val emotionList = arrayListOf<AnswerEmotion>().apply {
        defaultAnswerData.voice?.emotionList?.map { emotion ->
            val answerEmotion: AnswerEmotion? = when(emotion.type){
                EmotionType.ANGRY.name -> AnswerEmotion(EmotionType.ANGRY.name, angry)
                EmotionType.NEUTRAL.name -> AnswerEmotion(EmotionType.NEUTRAL.name, neutral)
                EmotionType.SADNESS.name -> AnswerEmotion(EmotionType.SADNESS.name, sadness)
                EmotionType.HAPPINESS.name -> AnswerEmotion(EmotionType.HAPPINESS.name, happiness)
                else -> null
            }
            answerEmotion?.let { add(answerEmotion) }
        }
    }

    return Answer(
        answerSeq = answerSeq,
        property = defaultAnswerData.property,
        question = defaultAnswerData.question,
        voice = AnswerVoice(
            text = text,
            emotion = emotion,
            emotionList = emotionList,
            duration = defaultAnswerData.voice?.duration ?: 0f,
            voiceUrl = defaultAnswerData.voice?.voiceUrl ?: "",
            waveformUrl = defaultAnswerData.voice?.waveformUrl ?: "",
        ),
        tagList = tags.split(","),
        shareType = defaultAnswerData.shareType,
        emotion = defaultAnswerData.emotion,
        timeAgo = defaultAnswerData.timeAgo,
        createdAt = defaultAnswerData.createdAt,
        firstDayInList = defaultAnswerData.firstDayInList
    )
}