plugins {
    `kotlin-dsl`
}

group = "mr.liks.mylittleagent.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    implementation(libs.room.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
    implementation("org.jetbrains.kotlin:kotlin-serialization:${libs.versions.kotlin.get()}")
    compileOnly("de.mannodermaus.gradle.plugins:android-junit5:${libs.versions.junit5Plugin.get()}")
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.mylittleagent.android.application.asProvider().get().pluginId
            implementationClass = "mr.liks.mylittleagent.buildlogic.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.mylittleagent.android.library.asProvider().get().pluginId
            implementationClass = "mr.liks.mylittleagent.buildlogic.AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = libs.plugins.mylittleagent.android.feature.get().pluginId
            implementationClass = "mr.liks.mylittleagent.buildlogic.AndroidFeatureConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = libs.plugins.mylittleagent.android.application.compose.get().pluginId
            implementationClass = "mr.liks.mylittleagent.buildlogic.AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = libs.plugins.mylittleagent.android.library.compose.get().pluginId
            implementationClass = "mr.liks.mylittleagent.buildlogic.AndroidLibraryComposeConventionPlugin"
        }
        register("androidRoom") {
            id = libs.plugins.mylittleagent.android.room.get().pluginId
            implementationClass = "mr.liks.mylittleagent.buildlogic.AndroidRoomConventionPlugin"
        }
        register("kotlinSerialization") {
            id = libs.plugins.mylittleagent.kotlin.serialization.get().pluginId
            implementationClass = "mr.liks.mylittleagent.buildlogic.KotlinSerializationConventionPlugin"
        }
        register("unitTest") {
            id = libs.plugins.mylittleagent.unit.test.get().pluginId
            implementationClass = "mr.liks.mylittleagent.buildlogic.UnitTestConventionPlugin"
        }
        register("unit4Test") {
            id = libs.plugins.mylittleagent.unit.test4.get().pluginId
            implementationClass = "mr.liks.mylittleagent.buildlogic.Unit4TestConventionPlugin"
        }
    }
}
