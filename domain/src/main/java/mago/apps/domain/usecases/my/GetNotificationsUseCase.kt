package mago.apps.domain.usecases.my

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.my.Notification
import mago.apps.domain.repository.MyRepository
import mago.apps.domain.repository.QuestionRepository
import javax.inject.Inject

/** @feature: 내 알림 목록 조회
 * @author: 2023/01/09 2:06 PM donghwishin
*/
class GetNotificationsUseCase @Inject constructor(
    private val myRepository: MyRepository
) {
    operator fun invoke(): Flow<PagingData<Notification>> {
        return myRepository.getNotifications()
    }
}