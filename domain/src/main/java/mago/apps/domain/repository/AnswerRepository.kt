package mago.apps.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.ApiResponse

interface AnswerRepository {

    /** @feature: 답변 정보를 조회하는 기능
     * @author: 2022/12/28 10:59 AM donghwishin
     * @description{
     *  answerSeq: Int // 질문 번호(seq)
     * }
     */
    suspend fun getAnswerInfo(answerSeq: Int): ApiResponse<Answer>


    /** @feature: 답변 목록을 조회하는 기능
     * @author: 2022/12/28 11:00 AM donghwishin
     * @description{
     *  isConnected true -> 우리의 감정주파수
     *  isConnected false -> 연결 제외
     *  isConnected null -> 전체 목록
     * }
     */
    fun getAnswerList(isConnected: Boolean?): Flow<PagingData<Answer>>

    /** @feature: 답변 수정 기능
     * @author: 2023/01/04 1:11 PM donghwishin
     * @description{
     *  answerSeq: Int // 질문 번호(seq)
     * }
     */
    suspend fun patchAnswer(
        answerSeq: Int,
        text: String,
        tags: String,
        anger: Int,
        neutral: Int,
        happiness: Int,
        sadness: Int,
    ): ApiResponse<Answer>

}