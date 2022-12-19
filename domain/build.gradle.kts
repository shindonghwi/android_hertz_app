plugins {
    id("kotlin")
    id("kotlin-kapt")
}

java {
    sourceCompatibility = AppConfig.javaVersion
    targetCompatibility = AppConfig.javaVersion
}

dependencies {
    Libraries.Hilt.apply {
        implementation(dagger)
    }

    Kapts.apply {
        Kapts.Hilt.apply {
            kapt(hiltCompiler)
        }
    }
}