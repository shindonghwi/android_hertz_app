package mago.apps.domain.usecases.answer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.Resource
import mago.apps.domain.repository.AnswerRepository
import javax.inject.Inject

/** @feature: 답변 수정기능
 * @author: 2023/01/04 1:26 PM donghwishin
 */
class PatchAnswerUseCase @Inject constructor(private val answerRepository: AnswerRepository) {

    suspend operator fun invoke(
        answerSeq: Int,
        text: String,
        tags: String,
        anger: Int,
        neutral: Int,
        happiness: Int,
        sadness: Int,
    ): Flow<Resource<Answer>> = flow {
        emit(Resource.Loading())
        try {
            val response =
                answerRepository.patchAnswer(
                    answerSeq,
                    text,
                    tags,
                    anger,
                    neutral,
                    happiness,
                    sadness
                )
            when (response.status) {
                200 -> emit(Resource.Success(response.message, response.data))
                else -> emit(Resource.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.toString()))
        }
    }
}