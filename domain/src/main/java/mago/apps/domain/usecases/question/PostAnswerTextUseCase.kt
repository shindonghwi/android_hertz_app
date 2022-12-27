package mago.apps.domain.usecases.question

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mago.apps.domain.model.common.EmotionType
import mago.apps.domain.model.common.Resource
import mago.apps.domain.repository.QuestionRepository
import javax.inject.Inject

class PostAnswerTextUseCase @Inject constructor(private val questionRepository: QuestionRepository) {

    suspend operator fun invoke(
        questionSeq: Int,
        text: String,
        emotion: EmotionType,
        tags: String?
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val response = questionRepository.postAnswerText(questionSeq, text, emotion, tags)
            when (response.status) {
                200 -> emit(Resource.Success(response.message, null))
                else -> emit(Resource.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.toString()))
        }
    }
}