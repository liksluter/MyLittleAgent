plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.mylittleagent.kotlin.serialization)
}
dependencies {
    api(libs.androidx.navigation3.runtime)
}
