package mago.apps.domain.repository

import mago.apps.domain.model.common.ApiResponse

interface MyRepository {

    /** @feature: 디바이스 등록
     * @author: 2023/01/05 4:46 PM donghwishin
    */
    suspend fun postDevice(deviceToken: String): ApiResponse<Unit>

}