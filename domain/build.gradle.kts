plugins {
    id("com.android.library")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "mago.apps.domain"
    compileSdk = AppConfig.compileSdk
    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
    }

    buildTypes {
        getByName("debug") {}
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = AppConfig.javaVersion
        targetCompatibility = AppConfig.javaVersion
    }
    kotlinOptions {
        jvmTarget = AppConfig.jvmTarget
    }
}

dependencies {
    Libraries.Coroutine.apply {
        implementation(core)
    }

    Libraries.OkHttp.apply {
        implementation(okhttp)
        implementation(logging)
    }

    Libraries.Retrofit.apply {
        implementation(retrofit)
        implementation(retrofit_gson)
    }

    Libraries.Hilt.apply {
        implementation(core)
    }

    Kapts.apply {
        Kapts.Hilt.apply {
            kapt(hiltCompiler)
        }
    }

}