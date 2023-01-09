package mago.apps.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mago.apps.domain.repository.QuestionRepository
import mago.apps.domain.usecases.question.*

@InstallIn(SingletonComponent::class)
@Module
object QuestionModule {

    @Provides
    fun provideGetQuestionRandomUseCase(questionRepository: QuestionRepository): GetQuestionRandomUseCase {
        return GetQuestionRandomUseCase(questionRepository)
    }

    @Provides
    fun provideGetQuestionInfoUseCase(questionRepository: QuestionRepository): GetQuestionInfoUseCase {
        return GetQuestionInfoUseCase(questionRepository)
    }

    @Provides
    fun providePostAnswerVoiceUseCase(questionRepository: QuestionRepository): PostAnswerVoiceUseCase {
        return PostAnswerVoiceUseCase(questionRepository)
    }

    @Provides
    fun providePostAnswerTextUseCase(questionRepository: QuestionRepository): PostAnswerTextUseCase {
        return PostAnswerTextUseCase(questionRepository)
    }

    @Provides
    fun provideDelLikeUseCase(questionRepository: QuestionRepository): DelLikeUseCase {
        return DelLikeUseCase(questionRepository)
    }

    @Provides
    fun providePostLikeUseCase(questionRepository: QuestionRepository): PostLikeUseCase {
        return PostLikeUseCase(questionRepository)
    }

    @Provides
    fun provideGetLikeListUseCase(questionRepository: QuestionRepository): GetLikeListUseCase {
        return GetLikeListUseCase(questionRepository)
    }

    @Provides
    fun providePostSendQuestionFriendUseCase(questionRepository: QuestionRepository): PostSendQuestionFriendUseCase {
        return PostSendQuestionFriendUseCase(questionRepository)
    }

}