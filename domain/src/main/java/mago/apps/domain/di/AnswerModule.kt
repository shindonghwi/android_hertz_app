package mago.apps.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mago.apps.domain.repository.AnswerRepository
import mago.apps.domain.usecases.answer.GetAnswerConnectedInfoUseCase
import mago.apps.domain.usecases.answer.GetAnswerInfoUseCase
import mago.apps.domain.usecases.answer.GetAnswerListUseCase
import mago.apps.domain.usecases.answer.PatchAnswerUseCase

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

    @Provides
    fun providePatchAnswerUseCase(answerRepository: AnswerRepository): PatchAnswerUseCase {
        return PatchAnswerUseCase(answerRepository)
    }

    @Provides
    fun provideGetAnswerConnectedInfoUseCase(answerRepository: AnswerRepository): GetAnswerConnectedInfoUseCase {
        return GetAnswerConnectedInfoUseCase(answerRepository)
    }

}