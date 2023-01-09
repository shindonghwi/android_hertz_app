package mago.apps.data.repository.my

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mago.apps.data.network.api.my.MyApi
import mago.apps.data.network.utils.SafeApiRequest
import mago.apps.data.repository.my.paging.NotificationPagingSource
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.model.my.Notification
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

    override fun getNotifications(): Flow<PagingData<Notification>> {
        return Pager(
            PagingConfig(pageSize = NotificationPagingSource.PAGING_SIZE)
        ) {
            NotificationPagingSource(myApi)
        }.flow
    }
}

