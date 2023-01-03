package mago.apps.domain.usecases.question

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.repository.QuestionRepository
import javax.inject.Inject

/** @feature: 질문 좋아요 목록 조회
 * @author: 2023/01/03 3:20 PM donghwishin
 */
class GetLikeListUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    operator fun invoke(): Flow<PagingData<Answer>> {
        return questionRepository.getLikes()
    }
}