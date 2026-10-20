plugins {
    alias(libs.plugins.mylittleagent.android.library)
}

android {
    namespace = "mr.liks.mylittleagent.llm"
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

    androidResources {
        noCompress.add("litertlm")
    }
}

dependencies {
    implementation(libs.koin.android)

    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.timber)

    implementation(libs.litertlm.android)
}