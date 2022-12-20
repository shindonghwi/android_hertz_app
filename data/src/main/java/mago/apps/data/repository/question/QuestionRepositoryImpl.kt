package mago.apps.data.repository.question

import mago.apps.data.mappers.answer.toDomain
import mago.apps.data.mappers.question.toDomain
import mago.apps.data.network.api.question.QuestionApi
import mago.apps.data.network.utils.SafeApiRequest
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.model.question.QuestionRandom
import mago.apps.domain.repository.QuestionRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(private val questionApi: QuestionApi) :
    QuestionRepository, SafeApiRequest() {
    override suspend fun getQuestionRandom(): ApiResponse<QuestionRandom> {
        val response = safeApiRequest { questionApi.getQuestionRandom() }
        return ApiResponse(
            status = response.status, message = response.message, data = response.data?.toDomain()
        )
    }

    override suspend fun postAnswerVoice(
        questionSeq: Int, file: MultipartBody.Part
    ): ApiResponse<Answer> {
        val response = safeApiRequest { questionApi.postAnswerVoice(questionSeq, file) }
        return ApiResponse(
            status = response.status, message = response.message, data = response.data?.toDomain()
        )
    }

    override suspend fun postAnswerText(
        questionSeq: Int, text: String, emotion: String, tags: String
    ): ApiResponse<Unit> {
        val response =
            safeApiRequest { questionApi.postAnswerText(questionSeq, text, emotion, tags) }
        return ApiResponse(
            status = response.status, message = response.message, data = null
        )
    }
}

