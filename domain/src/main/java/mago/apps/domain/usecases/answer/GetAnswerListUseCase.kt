package mago.apps.domain.usecases.answer

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.repository.AnswerRepository
import javax.inject.Inject

/** @feature: 답변 목록 조회
 * @author: 2022/12/28 11:22 AM donghwishin
 */
class GetAnswerListUseCase @Inject constructor(
    private val answerRepository: AnswerRepository
) {

    operator fun invoke(isConnected: Boolean?): Flow<PagingData<Answer>> {
        return answerRepository.getAnswerList(isConnected)
    }
}