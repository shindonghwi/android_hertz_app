package mago.apps.domain.usecases.answer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.DataListType
import mago.apps.domain.model.common.Resource
import mago.apps.domain.repository.AnswerRepository
import javax.inject.Inject

/** @feature: 답변 목록 조회
 * @author: 2022/12/28 11:22 AM donghwishin
 */
class GetAnswerListUseCase @Inject constructor(private val answerRepository: AnswerRepository) {

    suspend operator fun invoke(isConnected: Boolean?): Flow<Resource<DataListType<Answer>>> =
        flow {
            emit(Resource.Loading())
            try {
                val response = answerRepository.getAnswerList(isConnected)
                when (response.status) {
                    200 -> emit(Resource.Success(response.message, response.data))
                    else -> emit(Resource.Error(response.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(message = e.toString()))
            }
        }
}