package mago.apps.domain.repository

import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.model.login.Login

interface AuthRepository {

    suspend fun postLogin(id: String, password: String): ApiResponse<Login>

}