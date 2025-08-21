import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.plugin.extraProperties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.detekt)
    id("maven-publish")
    alias(libs.plugins.dependency.guard)
}

dependencyGuard {
    // Matches the variant that is published.
    configuration("releaseRuntimeClasspath")
}

android {
    namespace = "com.github.justeattakeaway.intervalannotatedstring"
    compileSdk = 36

    defaultConfig {
        version = extraProperties.get("publish.versionName").toString()

        minSdk = 21
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
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

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

publishing {
    publications {
        repositories {
            maven {
                name = "local"
                url = uri(layout.buildDirectory.dir("maven-local"))
            }

            maven {
                name = "maven-central"

                val releaseUrl = extraProperties.get("publish.repo.releaseUrl").toString()
                val snapshotUrl = extraProperties.get("publish.repo.snapshotUrl").toString()

                url = uri(if (BuildConfig.isSnapshot) snapshotUrl else releaseUrl)
                credentials {
                    val localProperties = gradleLocalProperties(project.rootDir, providers)
                    username = localProperties.getProperty("publish.repo.credentials.userName")
                    password = localProperties.getProperty("publish.repo.credentials.password")
                }
            }
        }

        register<MavenPublication>("release") {
            groupId = extraProperties.get("publish.groupId").toString()
            artifactId = extraProperties.get("publish.artifactId").toString()
            version = extraProperties.get("publish.versionName").toString()

            pom {
                name = extraProperties.get("publish.pom.name").toString()
                description = extraProperties.get("publish.pom.description").toString()
                url = extraProperties.get("publish.pom.url").toString()

                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
            }

            afterEvaluate {
                from(components["release"])
            }
        }
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
