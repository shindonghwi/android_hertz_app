package mago.apps.data.repository.answer

import mago.apps.data.mappers.answer.toDomain
import mago.apps.data.network.api.answer.AnswerApi
import mago.apps.data.network.utils.SafeApiRequest
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.model.common.DataListType
import mago.apps.domain.repository.AnswerRepository
import javax.inject.Inject

/** @feature: 답변 관련 API 기능 구현부
 * @author: 2022/12/28 11:13 AM donghwishin
 */
class AnswerRepositoryImpl @Inject constructor(private val answerApi: AnswerApi) :
    AnswerRepository, SafeApiRequest() {
    override suspend fun getAnswerInfo(answerSeq: Int): ApiResponse<Answer> {
        val response = safeApiRequest { answerApi.getAnswerInfo(answerSeq) }
        return ApiResponse(
            status = response.status, message = response.message, data = response.data?.toDomain()
        )
    }

    override suspend fun getAnswerList(isConnected: Boolean?): ApiResponse<DataListType<Answer>> {
        val response = safeApiRequest { answerApi.getAnswerList(isConnected) }
        return ApiResponse(
            status = response.status, message = response.message, data = response.data
        )
    }
}

