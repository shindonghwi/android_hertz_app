package mago.apps.domain.usecases.answer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.Resource
import mago.apps.domain.repository.AnswerRepository
import javax.inject.Inject

/** @feature: 답변 연결 정보
 * @author: 2023/01/09 4:11 PM donghwishin
 */
class GetAnswerConnectedInfoUseCase @Inject constructor(private val answerRepository: AnswerRepository) {

    suspend operator fun invoke(answerSeq: Int): Flow<Resource<Answer>> = flow {
        emit(Resource.Loading())
        try {
            val response = answerRepository.getAnswerConnectedInfo(answerSeq)
            when (response.status) {
                200 -> emit(Resource.Success(response.message, response.data))
                else -> emit(Resource.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.toString()))
        }
    }
}