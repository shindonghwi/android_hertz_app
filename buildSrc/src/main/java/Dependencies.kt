object Versions {

    object Gradle{
        const val version = "7.2.0"
    }

    object Kotlin{
        const val version = "1.7.10"
    }

    object Coroutine{
        const val version = "1.6.4"
    }

    object AndroidX{
        const val constraintLayout = "2.1.4"
    }

    object KTX{
        const val core = "1.7.0"
        const val lifecycleVersion = "2.5.1"
    }

    object Compose{
        const val composeCompiler = "1.3.1"
        const val compose = "1.2.1"
        const val navigation = "2.5.2"
        const val constraintLayout = "1.0.0-rc01"
        const val material3 = "1.0.1"
        const val accompanistPermission = "0.23.1"
        const val accompanistPager = "0.28.0"
        const val paging = "1.0.0-alpha17"
        const val coil = "2.2.2"
        const val swipeRefresh = "0.29.0-alpha"
    }

    object Paging{
        const val version = "3.1.1"
    }

    object Hilt{
        const val compose_hilt = "1.0.0"
        const val version = "2.44"
    }

    object OkHttp{
        const val version = "4.9.3"
        const val logging = "3.9.1"
    }

    object Retrofit{
        const val version = "2.6.0"
    }

    object Gson{
        const val version = "2.10"
    }

    object Firebase{
        const val bom = "31.1.0"
        const val messagingService = "23.1.1"
    }

    object Google{
        const val servicesVersion = "4.3.13"
    }
}

object Libraries {

    object AndroidX {
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.AndroidX.constraintLayout}"
    }

    object KTX {
        const val core = "androidx.core:core-ktx:${Versions.KTX.core}"
        const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.KTX.lifecycleVersion}"
        const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.KTX.lifecycleVersion}"
    }

    object Coroutine {
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Coroutine.version}"
    }

    object Compose {
        const val activity = "androidx.activity:activity-compose:${Versions.Compose.composeCompiler}"
        const val ui = "androidx.compose.ui:ui:${Versions.Compose.compose}"
        const val material = "androidx.compose.material:material:${Versions.Compose.compose}"
        const val uiTooling = "androidx.compose.ui:ui-tooling-preview:${Versions.Compose.compose}"
        const val navigation = "androidx.navigation:navigation-compose:${Versions.Compose.navigation}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:${Versions.Compose.constraintLayout}"
        const val animation = "androidx.compose.animation:animation:${Versions.Compose.compose}"
        const val material3 = "androidx.compose.material3:material3:${Versions.Compose.material3}"
        const val material3window = "androidx.compose.material3:material3-window-size-class:${Versions.Compose.material3}"
        const val accompanistPermission = "com.google.accompanist:accompanist-permissions:${Versions.Compose.accompanistPermission}"
        const val pager = "com.google.accompanist:accompanist-pager:${Versions.Compose.accompanistPager}"
        const val pagerPermission = "com.google.accompanist:accompanist-pager-indicators:${Versions.Compose.accompanistPager}"
        const val paging = "androidx.paging:paging-compose:${Versions.Compose.paging}"
        const val coil = "io.coil-kt:coil-compose:${Versions.Compose.coil}"
    }

    object Paging {
        const val pagingRuntime = "androidx.paging:paging-runtime:${Versions.Paging.version}"
        const val pagingCommon = "androidx.paging:paging-common:${Versions.Paging.version}"
    }

    object Hilt {
        const val NavigationCompose = "androidx.hilt:hilt-navigation-compose:${Versions.Hilt.compose_hilt}"
        const val dagger = "com.google.dagger:hilt-android:${Versions.Hilt.version}"
        const val core = "com.google.dagger:hilt-core:${Versions.Hilt.version}"
    }

    object OkHttp{
        const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.OkHttp.version}"
        const val logging = "com.squareup.okhttp3:logging-interceptor:${Versions.OkHttp.version}"
    }

    object Retrofit{
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.Retrofit.version}"
        const val retrofit_gson = "com.squareup.retrofit2:converter-gson:${Versions.Retrofit.version}"
    }

    object Gson{
        const val gson = "com.google.code.gson:gson:${Versions.Gson.version}"
    }

    object Firebase{
        const val bom = "com.google.firebase:firebase-bom:${Versions.Firebase.bom}"
        const val analytics = "com.google.firebase:firebase-analytics"
        const val messaging = "com.google.firebase:firebase-messaging:${Versions.Firebase.messagingService}"
    }
}

object ClassPaths{
    const val googleService = "com.google.gms:google-services:${Versions.Google.servicesVersion}"
    const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.Hilt.version}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin.version}"
    const val gradle = "com.android.tools.build:gradle:${Versions.Gradle.version}"
}

object Kapts {
    object Hilt{
        const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.Hilt.version}"
    }
}
