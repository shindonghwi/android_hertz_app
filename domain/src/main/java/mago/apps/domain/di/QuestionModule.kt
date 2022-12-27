package mago.apps.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mago.apps.domain.repository.QuestionRepository
import mago.apps.domain.usecases.question.GetQuestionRandomUseCase
import mago.apps.domain.usecases.question.PostAnswerTextUseCase
import mago.apps.domain.usecases.question.PostAnswerVoiceUseCase

@InstallIn(SingletonComponent::class)
@Module
object QuestionModule {

    @Provides
    fun provideGetRandomUseCase(questionRepository: QuestionRepository): GetQuestionRandomUseCase {
        return GetQuestionRandomUseCase(questionRepository)
    }

    @Provides
    fun providePostAnswerVoiceUseCase(questionRepository: QuestionRepository): PostAnswerVoiceUseCase {
        return PostAnswerVoiceUseCase(questionRepository)
    }

    @Provides
    fun providePostAnswerTextUseCase(questionRepository: QuestionRepository): PostAnswerTextUseCase {
        return PostAnswerTextUseCase(questionRepository)
    }

}