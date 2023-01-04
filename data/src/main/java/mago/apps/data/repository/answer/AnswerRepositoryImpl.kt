package mago.apps.data.repository.answer

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mago.apps.data.mappers.answer.toDomain
import mago.apps.data.network.api.answer.AnswerApi
import mago.apps.data.network.utils.SafeApiRequest
import mago.apps.data.repository.answer.paging.AnswerListPagingSource
import mago.apps.data.repository.answer.paging.AnswerListPagingSource.Companion.PAGING_SIZE
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.repository.AnswerRepository
import javax.inject.Inject

/** @feature: 답변 관련 API 기능 구현부
 * @author: 2022/12/28 11:13 AM donghwishin
 */
class AnswerRepositoryImpl @Inject constructor(
    private val answerApi: AnswerApi
) : AnswerRepository, SafeApiRequest() {

    override suspend fun getAnswerInfo(answerSeq: Int): ApiResponse<Answer> {
        val response = safeApiRequest { answerApi.getAnswerInfo(answerSeq) }
        return ApiResponse(
            status = response.status, message = response.message, data = response.data?.toDomain()
        )
    }

    override fun getAnswerList(isConnected: Boolean?): Flow<PagingData<Answer>> {
        return Pager(
            PagingConfig(pageSize = PAGING_SIZE)
        ) {
            AnswerListPagingSource(answerApi, isConnected)
        }.flow
    }

    override suspend fun patchAnswer(
        answerSeq: Int,
        text: String,
        tags: String,
        anger: Int,
        neutral: Int,
        happiness: Int,
        sadness: Int
    ): ApiResponse<Answer> {
        val response = safeApiRequest {
            answerApi.patchAnswer(
                answerSeq,
                text,
                tags,
                anger,
                neutral,
                happiness,
                sadness
            )
        }
        return ApiResponse(
            status = response.status, message = response.message, data = response.data?.toDomain()
        )
    }
}

