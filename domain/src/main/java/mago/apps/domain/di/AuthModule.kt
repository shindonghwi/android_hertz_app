package mago.apps.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mago.apps.domain.repository.AuthRepository
import mago.apps.domain.usecases.auth.PostLoginUseCase

@InstallIn(SingletonComponent::class)
@Module
object AuthModule {

    @Provides
    fun providePostLoginUseCase(authRepository: AuthRepository): PostLoginUseCase {
        return PostLoginUseCase(authRepository)
    }

}