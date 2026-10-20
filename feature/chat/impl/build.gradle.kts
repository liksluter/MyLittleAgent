plugins {
    alias(libs.plugins.mylittleagent.android.feature)
    alias(libs.plugins.mylittleagent.unit.test4)
    alias(libs.plugins.roborazzi.plugin)
}

android {
    namespace = "mr.liks.feature.chat.impl"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":feature:chat:api"))
    implementation(project(":core:database"))
    implementation(project(":core:llm"))

    implementation(libs.androidx.navigation3.runtime)

    implementation(libs.timber)
}