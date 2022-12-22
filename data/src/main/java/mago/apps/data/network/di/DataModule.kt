package mago.apps.data.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mago.apps.data.constants.*
import mago.apps.data.network.api.auth.AuthApi
import mago.apps.data.network.api.question.QuestionApi
import mago.apps.data.repository.auth.AuthRepositoryImpl
import mago.apps.data.repository.question.QuestionRepositoryImpl
import mago.apps.domain.repository.AuthRepository
import mago.apps.domain.repository.QuestionRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader(HEADER_KEY, HEADER_VALUE)
                .addHeader(HEADER_AUTH_KEY, HEADER_AUTH_VALUE)
                .build()
            proceed(newRequest)
        }
    }

    private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient
            = OkHttpClient.Builder().run {
        addInterceptor(interceptor)
        build()
    }


    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    fun provideAuthRepository(authApi: AuthApi): AuthRepository{
        return AuthRepositoryImpl(authApi)
    }

    @Provides
    fun provideQuestionApi(retrofit: Retrofit): QuestionApi = retrofit.create(QuestionApi::class.java)

    @Provides
    fun provideQuestionRepository(questionApi: QuestionApi): QuestionRepository{
        return QuestionRepositoryImpl(questionApi)
    }
}