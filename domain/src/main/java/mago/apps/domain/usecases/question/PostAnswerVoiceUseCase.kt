package mago.apps.domain.usecases.question

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.Resource
import mago.apps.domain.repository.QuestionRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class PostAnswerVoiceUseCase @Inject constructor(private val questionRepository: QuestionRepository) {

    suspend operator fun invoke(
        questionSeq: Int,
        file: MultipartBody.Part
    ): Flow<Resource<Answer>> = flow {
        emit(Resource.Loading())
        try {
            val response = questionRepository.postAnswerVoice(questionSeq, file)
            when (response.status) {
                200 -> emit(Resource.Success(response.message, response.data))
                else -> emit(Resource.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.toString()))
        }
    }
}