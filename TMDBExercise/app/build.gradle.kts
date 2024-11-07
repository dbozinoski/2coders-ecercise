import org.jetbrains.kotlin.fir.declarations.builder.buildScript

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    alias(libs.plugins.com.google.dagger.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktlint)
}

ktlint {
    version.set(libs.versions.ktlint.get()) // pulls the version from the catalog
    android.set(true) // Enable Android-specific rules
    outputColorName.set("RED") // Optional, to color output in logs
}

android {
    namespace = "com.example.tmdbexercise"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tmdbexercise"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint") // Version should be inherited from parent

    repositories {
        // Required to download KtLint
        mavenCentral()
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.lifecycleViewmodel)
    implementation(libs.lifecycleLivedata)
    implementation(libs.lifecycleViewmodelSavedstate)
    kapt(libs.lifecycleCompiler)
    implementation(libs.kotlinxCoroutinesCore)
    implementation(libs.kotlinxCoroutinesAndroid)
    implementation(libs.roomRuntime)
    kapt(libs.roomCompiler)
    implementation(libs.roomKtx)
    implementation(libs.retrofit)
    implementation(libs.converterGson)
    implementation(libs.loggingInterceptor)
    implementation(libs.hilt)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    implementation (libs.androidx.runtime.livedata)
    implementation (libs.glide.compose)
    kapt(libs.hiltCompiler)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.foundation)
    testImplementation(libs.junit)
    // Coroutine testing
    testImplementation(libs.kotlinx.coroutines.test)
    // Paging library
    testImplementation(libs.androidx.paging.common)
    // Mocking framework
    testImplementation(libs.mockk)
    // Turbine for Flow testing
    testImplementation(libs.app.turbine)
    testImplementation(libs.androidx.paging.testing)
    // Coroutines Test
    testImplementation (libs.kotlinx.coroutines.test)
    // For LiveData testing
    testImplementation (libs.androidx.core.testing)
    // For Truth (Google's assertion library)
    testImplementation (libs.google.truth)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}