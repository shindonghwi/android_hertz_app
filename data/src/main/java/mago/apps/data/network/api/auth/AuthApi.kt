package mago.apps.data.network.api.auth

import mago.apps.data.constants.API_VERSION
import mago.apps.data.network.model.LoginDTO
import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.model.common.Resource
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {

    @POST("$API_VERSION/auth/login")
    @FormUrlEncoded
    suspend fun postLogin(
        @Field("id") id: String,
        @Field("password") password: String,
    ): Response<ApiResponse<LoginDTO>>

}