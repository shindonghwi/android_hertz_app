package mago.apps.data.network.api.question

import mago.apps.data.constants.API_VERSION
import mago.apps.data.network.model.answer.AnswerDTO
import mago.apps.data.network.model.question.QuestionRandomDTO
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.ApiResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface QuestionApi {

    @GET("$API_VERSION/questions/random")
    suspend fun getQuestionRandom(): Response<ApiResponse<QuestionRandomDTO>>

    @POST("$API_VERSION/question/{questionSeq}/answer/voice")
    suspend fun postAnswerVoice(
        @Path(value = "questionSeq") questionSeq: Int,
        @Part file: MultipartBody.Part
    ): Response<ApiResponse<AnswerDTO>>

    @POST("$API_VERSION/question/{questionSeq}/answer/text")
    suspend fun postAnswerText(
        @Path(value = "questionSeq") questionSeq: Int,
        @Field("text") text: String,
        @Field("emotion") emotion: String,
        @Field("tags") tags: String,
    ): Response<ApiResponse<Unit>>

}