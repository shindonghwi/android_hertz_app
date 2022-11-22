plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
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
        }

        Libraries.Hilt.apply {
            implementation(NavigationCompose)
            implementation(dagger)
        }
        implementation("com.google.accompanist:accompanist-permissions:0.27.1")
    }

    Kapts.apply {
        Kapts.Hilt.apply {
            kapt(hiltCompiler)
        }
    }
}
