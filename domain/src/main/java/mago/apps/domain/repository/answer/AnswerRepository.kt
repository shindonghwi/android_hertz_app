package mago.apps.domain.repository.answer

import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.model.common.DataListType

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
    suspend fun getAnswerList(
        isConnected: Boolean?,
        page: Int = 1,
        size: Int = 20,
        offsetTime: Long? = null
    ): ApiResponse<DataListType<Answer>>

}