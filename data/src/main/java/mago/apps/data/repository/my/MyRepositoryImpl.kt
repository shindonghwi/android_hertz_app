package mago.apps.data.repository.my

import mago.apps.data.network.api.my.MyApi
import mago.apps.data.network.utils.SafeApiRequest
import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.repository.MyRepository
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(private val myApi: MyApi) :
    MyRepository, SafeApiRequest() {

    override suspend fun postDevice(deviceToken: String): ApiResponse<Unit> {
        val response = safeApiRequest { myApi.postDevice(deviceToken) }
        return ApiResponse(
            status = response.status, message = response.message, data = null
        )
    }
}

