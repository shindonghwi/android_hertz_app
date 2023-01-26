package mago.apps.domain.usecases.question

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mago.apps.domain.model.common.Resource
import mago.apps.domain.model.question.QuestionRandom
import mago.apps.domain.repository.QuestionRepository
import javax.inject.Inject

class GetQuestionRandomUseCase @Inject constructor(private val questionRepository: QuestionRepository) {

    suspend operator fun invoke(): Flow<Resource<QuestionRandom>> = flow {
        emit(Resource.Loading())
        try {
            val response = questionRepository.getQuestionRandom()
            when (response.status) {
                200 -> emit(Resource.Success(response.message, response.data))
                else -> emit(Resource.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.toString()))
        }
    }
}