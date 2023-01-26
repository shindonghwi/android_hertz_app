package mago.apps.data.network.api.my

import mago.apps.data.constants.API_VERSION
import mago.apps.data.network.model.answer.AnswerDTO
import mago.apps.data.network.model.my.NotificationDTO
import mago.apps.data.network.model.question.QuestionRandomDTO
import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.model.common.DataListType
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface MyApi {

    /** @feature: 디바이스 등록
     * @author: 2023/01/05 4:47 PM donghwishin
    */
    @POST("$API_VERSION/me/device")
    @FormUrlEncoded
    suspend fun postDevice(
        @Field("deviceToken") deviceToken: String,
    ): Response<ApiResponse<Unit>>


    /** @feature: 알림 목록 조회
     * @author: 2023/01/09 2:00 PM donghwishin
    */
    @GET("$API_VERSION/me/notifications")
    suspend fun getNotifications(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("offsetTime") offsetTime: Long?,
    ): ApiResponse<DataListType<NotificationDTO>>
}