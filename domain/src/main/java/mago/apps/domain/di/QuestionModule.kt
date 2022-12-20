package mago.apps.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mago.apps.domain.repository.QuestionRepository
import mago.apps.domain.usecases.question.GetQuestionRandomUseCase

@InstallIn(SingletonComponent::class)
@Module
object QuestionModule {

    @Provides
    fun provideGetRandomUseCase(questionRepository: QuestionRepository): GetQuestionRandomUseCase {
        return GetQuestionRandomUseCase(questionRepository)
    }

}