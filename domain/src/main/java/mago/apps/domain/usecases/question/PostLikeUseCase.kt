package mago.apps.domain.usecases.question

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.Resource
import mago.apps.domain.repository.QuestionRepository
import javax.inject.Inject

/** @feature: 질문 좋아요 등록
 * @author: 2023/01/03 3:19 PM donghwishin
 */
class PostLikeUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    suspend operator fun invoke(questionSeq: Int): Flow<Resource<Answer>> = flow {
        Log.w("Asdasdasd", "invoke: start", )
        emit(Resource.Loading())
        try {
            val response = questionRepository.postLike(questionSeq)
            when (response.status) {
                200 -> emit(Resource.Success(response.message, null))
                else -> emit(Resource.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.toString()))
        }
    }
}