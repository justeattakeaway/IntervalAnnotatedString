import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.justeattakeaway.intervalannotatedstring"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
        consumerProguardFiles("consumer-rules.pro")
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.toString()
    }
}

dependencies {
    detektPlugins(libs.detekt.formatting)

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.ui)

    testImplementation(libs.junit)
    testImplementation(libs.junitparams)
    testImplementation(libs.mockk)
}
