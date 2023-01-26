package mago.apps.hertz.di

import android.app.Application
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mago.apps.domain.usecases.my.PostDeviceUseCase
import mago.apps.hertz.firebase.FCMUtil
import mago.apps.hertz.firebase.NotificationHelper
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {

    @Provides
    @Singleton
    fun analytics(application: Application): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(application)
    }

    @Provides
    @Singleton
    fun fcmUtilInstance(postDeviceUseCase: PostDeviceUseCase): FCMUtil {
        return FCMUtil(postDeviceUseCase)
    }

    @Provides
    @Singleton
    fun notificationHelperInstance(@ApplicationContext context: Context): NotificationHelper {
        return NotificationHelper(context)
    }

}