package mago.apps.data.repository.my.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import mago.apps.data.mappers.my.toDomain
import mago.apps.data.network.api.my.MyApi
import mago.apps.data.network.model.my.NotificationDTO
import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.model.common.DataListType
import mago.apps.domain.model.my.Notification

/** @feature: 알림 목록을 가져오는 기능
 * @author: 2023/01/09 1:59 PM donghwishin
 */
class NotificationPagingSource(
    private val myApi: MyApi
) : PagingSource<Int, Notification>() {

    private var offsetTime: Long? = null

    override fun getRefreshKey(state: PagingState<Int, Notification>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
        try {
            val nextPage = params.key ?: 1

            val response: ApiResponse<DataListType<NotificationDTO>> = myApi.getNotifications(
                nextPage,
                PAGING_SIZE,
                offsetTime
            )

            return when (response.status) {
                200 -> {
                    offsetTime = response.data!!.page.offsetTime

                    LoadResult.Page(
                        data = response.data!!.items.map { it.toDomain() },
                        prevKey = if (nextPage == 1) null else nextPage - 1,
                        nextKey = if (response.data!!.page.hasNext) {
                            response.data!!.page.currentPage + 1
                        } else {
                            null
                        }
                    )
                }
                else -> {
                    LoadResult.Error(Throwable(message = response.message))
                }
            }
        } catch (exception: Exception) {
            Throwable(exception.message.toString())
            return LoadResult.Error(Throwable(exception.message.toString()))
        }
    }

    companion object {
        const val PAGING_SIZE = 20
    }
}