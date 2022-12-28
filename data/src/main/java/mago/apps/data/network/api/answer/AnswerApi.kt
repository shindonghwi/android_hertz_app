package mago.apps.data.network.api.answer

import mago.apps.data.constants.API_VERSION
import mago.apps.data.network.model.answer.AnswerDTO
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.model.common.DataListType
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnswerApi {

    /** @feature: 답변 정보를 조회하는 기능
     * @author: 2022/12/28 11:07 AM donghwishin
     * @description{
     *  answerSeq: Int // 질문 번호(seq)
     * }
    */
    @GET("$API_VERSION/answer/{answerSeq}")
    suspend fun getAnswerInfo(
        @Path(value = "answerSeq") answerSeq: Int,
    ): Response<ApiResponse<AnswerDTO>>


    /** @feature: 답변 목록을 조회하는 기능
     * @author: 2022/12/28 11:06 AM donghwishin
     * @description{
     *  isConnected true -> 우리의 감정주파수
     *  isConnected false -> 연결 제외
     *  isConnected null -> 전체 목록
     * }
    */
    @GET("$API_VERSION/answers")
    suspend fun getAnswerList(
        @Query("isConnected") isConnected: Boolean?,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("offsetTime") offsetTime: Long?,
    ): ApiResponse<DataListType<AnswerDTO>>
}
