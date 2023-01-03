package mago.apps.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.model.common.ApiResponse
import mago.apps.domain.model.common.DataListType
import mago.apps.domain.model.common.EmotionType
import mago.apps.domain.model.question.QuestionRandom
import okhttp3.MultipartBody

interface QuestionRepository {

    /** @feature: 질문 랜덤 조회
     * @author: 2023/01/03 2:50 PM donghwishin
     */
    suspend fun getQuestionRandom(): ApiResponse<QuestionRandom>

    /** @feature: 음성 답변 등록
     * @author: 2023/01/03 2:50 PM donghwishin
     */
    suspend fun postAnswerVoice(
        questionSeq: Int,
        file: MultipartBody.Part
    ): ApiResponse<Answer>

    /** @feature: 문자 답변 등록
     * @author: 2023/01/03 2:50 PM donghwishin
     */
    suspend fun postAnswerText(
        questionSeq: Int,
        text: String,
        emotion: EmotionType,
        tags: String?
    ): ApiResponse<Answer>

    /** @feature: 질문 전송(삐삐) 등록
     * @author: 2023/01/03 3:01 PM donghwishin
     */
    suspend fun postSendQuestionFriend(
        questionSeq: Int
    ): ApiResponse<Unit>

    /** @feature: 질문 좋아요 목록 조회
     * @author: 2023/01/03 3:01 PM donghwishin
     */
    fun getLikes(): Flow<PagingData<Answer>>

    /** @feature: 질문 좋아요 등록
     * @author: 2023/01/03 3:04 PM donghwishin
     */
    suspend fun postLike(
        questionSeq: Int
    ): ApiResponse<Unit>

    /** @feature: 질문 좋아요 취소
     * @author: 2023/01/03 3:06 PM donghwishin
     */
    suspend fun delLike(
        questionSeq: Int
    ): ApiResponse<Unit>

}