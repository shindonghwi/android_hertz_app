package mago.apps.data.network.api.question

import mago.apps.data.constants.API_VERSION
import mago.apps.data.network.model.answer.AnswerDTO
import mago.apps.data.network.model.question.QuestionRandomDTO
import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.model.common.DataListType
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface QuestionApi {

    /** @feature: 질문 랜덤 조회
     * @author: 2023/01/03 2:50 PM donghwishin
     */
    @GET("$API_VERSION/questions/random")
    suspend fun getQuestionRandom(): Response<ApiResponse<QuestionRandomDTO>>

    /** @feature: 음성 답변 등록
     * @author: 2023/01/03 2:50 PM donghwishin
     */
    @Multipart
    @POST("$API_VERSION/question/{questionSeq}/answer/voice")
    suspend fun postAnswerVoice(
        @Path(value = "questionSeq") questionSeq: Int,
        @Part file: MultipartBody.Part
    ): Response<ApiResponse<AnswerDTO>>

    /** @feature: 문자 답변 등록
     * @author: 2023/01/03 2:50 PM donghwishin
     */
    @POST("$API_VERSION/question/{questionSeq}/answer/text")
    @FormUrlEncoded
    suspend fun postAnswerText(
        @Path(value = "questionSeq") questionSeq: Int,
        @Field("text") text: String,
        @Field("emotion") emotion: String,
        @Field("tags") tags: String?,
    ): Response<ApiResponse<AnswerDTO>>

    /** @feature: 질문 전송(삐삐) 등록
     * @author: 2023/01/03 3:01 PM donghwishin
     */
    @POST("$API_VERSION/question/{questionSeq}/send/friend")
    suspend fun postSendQuestionFriend(
        @Path(value = "questionSeq") questionSeq: Int,
    ): Response<ApiResponse<Unit>>

    /** @feature: 질문 좋아요 목록 조회
     * @author: 2023/01/03 3:01 PM donghwishin
     */
    @GET("$API_VERSION/question/likes")
    suspend fun getLikes(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("offsetTime") offsetTime: Long?,
    ): ApiResponse<DataListType<AnswerDTO>>

    /** @feature: 질문 좋아요 등록
     * @author: 2023/01/03 3:04 PM donghwishin
     */
    @POST("$API_VERSION/question/{questionSeq}/like")
    suspend fun postLike(
        @Path(value = "questionSeq") questionSeq: Int,
    ): Response<ApiResponse<Unit>>

    /** @feature: 질문 좋아요 취소
     * @author: 2023/01/03 3:06 PM donghwishin
     */
    @DELETE("$API_VERSION/question/{questionSeq}/like")
    suspend fun delLike(
        @Path(value = "questionSeq") questionSeq: Int,
    ): Response<ApiResponse<Unit>>

}