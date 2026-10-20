plugins {
    alias(libs.plugins.mylittleagent.android.application)
    alias(libs.plugins.mylittleagent.android.application.compose)
    alias(libs.plugins.mylittleagent.kotlin.serialization)
    alias(libs.plugins.mylittleagent.unit.test)
}

android {
    namespace = "mr.liks.mylittleagent.app"
    defaultConfig {
        applicationId = "mr.liks.mylittleagent"
    }
}

dependencies {
    implementation(project(":feature:chat:api"))
    implementation(project(":feature:chat:impl"))
    implementation(project(":core:database"))
    implementation(project(":core:llm"))

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)

    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.timber)
}