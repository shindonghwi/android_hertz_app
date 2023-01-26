package mago.apps.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mago.apps.domain.repository.MyRepository
import mago.apps.domain.usecases.my.GetNotificationsUseCase
import mago.apps.domain.usecases.my.PostDeviceUseCase

@InstallIn(SingletonComponent::class)
@Module
object MyModule {

    @Provides
    fun providePostDeviceUseCase(myRepository: MyRepository): PostDeviceUseCase {
        return PostDeviceUseCase(myRepository)
    }

    @Provides
    fun provideGetNotificationsUseCase(myRepository: MyRepository): GetNotificationsUseCase {
        return GetNotificationsUseCase(myRepository)
    }

}