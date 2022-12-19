package mago.apps.domain.di

import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mago.apps.domain.repository.LoginRepository
import mago.apps.domain.usecase.PostLoginUseCase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@dagger.Module
class UseCaseModule {

    @Provides
    @Singleton
    fun providePostLoginUseCase(loginRepository: LoginRepository): PostLoginUseCase {
        return PostLoginUseCase(loginRepository)
    }

}