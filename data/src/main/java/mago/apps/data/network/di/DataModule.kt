package mago.apps.data.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mago.apps.data.constants.*
import mago.apps.data.network.api.answer.AnswerApi
import mago.apps.data.network.api.auth.AuthApi
import mago.apps.data.network.api.my.MyApi
import mago.apps.data.network.api.question.QuestionApi
import mago.apps.data.repository.answer.AnswerRepositoryImpl
import mago.apps.data.repository.auth.AuthRepositoryImpl
import mago.apps.data.repository.my.MyRepositoryImpl
import mago.apps.data.repository.question.QuestionRepositoryImpl
import mago.apps.domain.repository.AnswerRepository
import mago.apps.domain.repository.AuthRepository
import mago.apps.domain.repository.MyRepository
import mago.apps.domain.repository.QuestionRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttpClient(AppInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader(HEADER_KEY, HEADER_VALUE)
                .addHeader(HEADER_AUTH_KEY, HEADER_AUTH_VALUE)
                .build()
            proceed(newRequest)
        }
    }

    private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient =
        OkHttpClient.Builder().run {
            addInterceptor(interceptor)
            connectTimeout(100, TimeUnit.SECONDS)
            readTimeout(300, TimeUnit.SECONDS)
            writeTimeout(300, TimeUnit.SECONDS)
            build()
        }


    /** @feature: 권한 관련 api, repository
     * @author: 2022/12/28 11:14 AM donghwishin
     */
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    fun provideAuthRepository(authApi: AuthApi): AuthRepository {
        return AuthRepositoryImpl(authApi)
    }

    /** @feature: 질문 관련 api, repository
     * @author: 2022/12/28 11:14 AM donghwishin
     */
    @Provides
    fun provideQuestionApi(retrofit: Retrofit): QuestionApi =
        retrofit.create(QuestionApi::class.java)

    @Provides
    fun provideQuestionRepository(questionApi: QuestionApi): QuestionRepository {
        return QuestionRepositoryImpl(questionApi)
    }

    /** @feature: 답변 관련 api, repository
     * @author: 2022/12/28 11:14 AM donghwishin
     */
    @Provides
    fun provideAnswerApi(retrofit: Retrofit): AnswerApi = retrofit.create(AnswerApi::class.java)

    @Provides
    fun provideAnswerRepository(answerApi: AnswerApi): AnswerRepository {
        return AnswerRepositoryImpl(answerApi)
    }

    /** @feature: 내 정보 관련 api, repository
     * @author: 2023/01/05 7:05 PM donghwishin
     */
    @Provides
    fun provideMyApi(retrofit: Retrofit): MyApi = retrofit.create(MyApi::class.java)

    @Provides
    fun provideMyRepository(myApi: MyApi): MyRepository {
        return MyRepositoryImpl(myApi)
    }

}