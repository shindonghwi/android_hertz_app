plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}


android {
    namespace = "mago.apps.hertz"
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = "mago.apps.hertz"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.Compose.composeCompiler
    }
}

dependencies {

    implementation(project(":data"))
    implementation(project(":domain"))

    Libraries.apply {

        Libraries.KTX.apply {
            implementation(core)
            implementation(lifecycleRuntime)
            implementation(lifecycleViewModel)
        }

        Libraries.Compose.apply {
            implementation(material3)
            implementation(material3window)
            implementation(activity)
            implementation(ui)
            implementation(uiTooling)
            implementation(navigation)
            implementation(constraintLayout)
            implementation(accompanistPermission)
            implementation(pager)
            implementation(pagerPermission)
            implementation(paging)
        }

        Libraries.OkHttp.apply {
            implementation(okhttp)
            implementation(logging)
        }

        Libraries.Paging.apply {
            implementation(pagingRuntime)
        }

        Libraries.Retrofit.apply {
            implementation(retrofit)
            implementation(retrofit_gson)
        }

        Libraries.Hilt.apply {
            implementation(NavigationCompose)
            implementation(dagger)
        }
        Libraries.Firebase.apply {
            implementation(platform(bom))
            implementation(analytics)
        }
    }

    Kapts.apply {
        Kapts.Hilt.apply {
            kapt(hiltCompiler)
        }
    }
}
