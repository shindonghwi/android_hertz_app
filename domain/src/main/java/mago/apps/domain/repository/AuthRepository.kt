package mago.apps.domain.repository

import mago.apps.domain.model.auth.Login
import mago.apps.domain.model.common.ApiResponse

interface AuthRepository {

    suspend fun postLogin(id: String, password: String): ApiResponse<Login>

}