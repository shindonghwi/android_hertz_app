package mago.apps.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mago.apps.domain.repository.AnswerRepository
import mago.apps.domain.usecases.answer.GetAnswerInfoUseCase
import mago.apps.domain.usecases.answer.GetAnswerListUseCase

@InstallIn(SingletonComponent::class)
@Module
object AnswerModule {

    @Provides
    fun provideGetAnswerInfoUseCase(answerRepository: AnswerRepository): GetAnswerInfoUseCase {
        return GetAnswerInfoUseCase(answerRepository)
    }

    @Provides
    fun provideGetAnswerListUseCase(answerRepository: AnswerRepository): GetAnswerListUseCase {
        return GetAnswerListUseCase(answerRepository)
    }

}