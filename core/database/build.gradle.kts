plugins {
    alias(libs.plugins.mylittleagent.android.library)
    alias(libs.plugins.mylittleagent.android.room)
    alias(libs.plugins.mylittleagent.unit.test)
    alias(libs.plugins.tech.apter.robolectric)
}

android {
    namespace = "mr.liks.mylittleagent.core.database"
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
}

dependencies {
    implementation(libs.koin.android)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.timber)

    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.robolectric)
}