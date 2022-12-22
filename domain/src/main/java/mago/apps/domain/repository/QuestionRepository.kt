package mago.apps.domain.repository

import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.model.question.QuestionRandom
import okhttp3.MultipartBody

interface QuestionRepository {

    suspend fun getQuestionRandom(): ApiResponse<QuestionRandom>

    suspend fun postAnswerVoice(
        questionSeq: Int,
        file: MultipartBody.Part
    ): ApiResponse<Answer>

    suspend fun postAnswerText(
        questionSeq: Int,
        text: String,
        emotion: String,
        tags: String
    ): ApiResponse<Unit>

}