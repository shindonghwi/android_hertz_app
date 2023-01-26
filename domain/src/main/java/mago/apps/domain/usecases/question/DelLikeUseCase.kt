package mago.apps.domain.usecases.question

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.Resource
import mago.apps.domain.repository.QuestionRepository
import javax.inject.Inject

/** @feature: 질문 좋아요 취소
 * @author: 2023/01/03 3:20 PM donghwishin
 */
class DelLikeUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    suspend operator fun invoke(questionSeq: Int): Flow<Resource<Answer>> = flow {
        emit(Resource.Loading())
        try {
            val response = questionRepository.delLike(questionSeq)
            when (response.status) {
                200 -> emit(Resource.Success(response.message, null))
                else -> emit(Resource.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.toString()))
        }
    }
}