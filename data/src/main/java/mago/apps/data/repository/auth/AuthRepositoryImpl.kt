package mago.apps.data.repository.auth

import mago.apps.data.mappers.toDomain
import mago.apps.data.network.api.auth.AuthApi
import mago.apps.data.network.utils.SafeApiRequest
import mago.apps.domain.model.auth.Login
import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authApi: AuthApi) :
    AuthRepository, SafeApiRequest() {
    override suspend fun postLogin(id: String, password: String): ApiResponse<Login> {
        val response = safeApiRequest { authApi.postLogin(id, password) }
        return ApiResponse(
            status = response.status,
            message = response.message,
            data = response.data?.toDomain()
        )
    }
}