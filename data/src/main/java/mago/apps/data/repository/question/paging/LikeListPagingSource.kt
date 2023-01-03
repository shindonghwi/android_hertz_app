package mago.apps.data.repository.question.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import mago.apps.data.mappers.answer.toDomain
import mago.apps.data.network.api.question.QuestionApi
import mago.apps.data.network.model.answer.AnswerDTO
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.model.common.DataListType

/** @feature: 좋아요 목록을 가져오는 페이징 기능
 * @author: 2023/01/03 3:17 PM donghwishin
 */
class LikeListPagingSource(
    private val questionApi: QuestionApi,
) : PagingSource<Int, Answer>() {

    private var offsetTime: Long? = null

    override fun getRefreshKey(state: PagingState<Int, Answer>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Answer> {
        try {
            val nextPage = params.key ?: 1

            val response: ApiResponse<DataListType<AnswerDTO>> = questionApi.getLikes(
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