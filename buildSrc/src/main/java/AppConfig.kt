import org.gradle.api.JavaVersion

object AppConfig {
    const val compileSdk = 33
    const val minSdk = 26
    const val targetSdk = 33
    const val versionCode = 2
    const val versionName = "0.0.2"
    val javaVersion = JavaVersion.VERSION_11
    val jvmTarget = "11"
}

object DebugConfig{
    const val app_label = "Hertz(DEV)"
    const val suffixName = ""
    const val versionName = ""
}

object ReleaseConfig{
    const val app_label = "Hertz"
    const val suffixName = ""
    const val versionName = ""
}