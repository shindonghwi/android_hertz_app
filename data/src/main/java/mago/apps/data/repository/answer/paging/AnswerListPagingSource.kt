package mago.apps.data.repository.answer.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import mago.apps.data.mappers.answer.toDomain
import mago.apps.data.network.api.answer.AnswerApi
import mago.apps.data.network.model.answer.AnswerDTO
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.model.common.DataListType

/** @feature: 답변 목록을 조회하는 기능 정의
 * @author: 2022/12/28 8:01 PM donghwishin
 * @description{
 *  스크롤 최 하단으로 내려가면 20개의 데이터씩 불러온다.
 * }
 */
class AnswerListPagingSource(
    private val answerApi: AnswerApi,
    private val isConnected: Boolean?,
) : PagingSource<Int, Answer>() {

    private var offsetTime: Long? = null
    private var timeAgoHashMap = HashMap<Int, String>()

    override fun getRefreshKey(state: PagingState<Int, Answer>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Answer> {
        try {
            val nextPage = params.key ?: 1

            val response: ApiResponse<DataListType<AnswerDTO>> =
                answerApi.getAnswerList(
                    isConnected,
                    nextPage,
                    PAGING_SIZE,
                    offsetTime
                )

            return when (response.status) {
                200 -> {
                    offsetTime = response.data!!.page.offsetTime
                    val answerItems = response.data!!.items
                        .map { it.toDomain() }
                        .apply {
                            mapIndexed { index, answer ->
                                if (timeAgoHashMap.values.none { answer.timeAgo == it }) {
                                    timeAgoHashMap[index] = answer.timeAgo
                                    answer.timeAgoDisplay = answer.timeAgo
                                } else {
                                    timeAgoHashMap[index] = ""
                                    answer.timeAgoDisplay = null
                                }
                            }
                        }

                    LoadResult.Page(
                        data = answerItems,
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