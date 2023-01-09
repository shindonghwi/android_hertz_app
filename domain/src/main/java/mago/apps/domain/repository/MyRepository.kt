package mago.apps.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.model.my.Notification

interface MyRepository {

    /** @feature: 디바이스 등록
     * @author: 2023/01/05 4:46 PM donghwishin
    */
    suspend fun postDevice(deviceToken: String): ApiResponse<Unit>

    /** @feature: 내 디바이스 등록
     * @author: 2023/01/09 1:55 PM donghwishin
    */
    fun getNotifications(): Flow<PagingData<Notification>>
}