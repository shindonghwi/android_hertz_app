plugins {
    id("kotlin")
    id("kotlin-kapt")
}

java {
    sourceCompatibility = AppConfig.javaVersion
    targetCompatibility = AppConfig.javaVersion
}

dependencies {
    implementation(project(":domain"))

}