import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.extraProperties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.detekt)
    alias(libs.plugins.dependency.guard)
    id("maven-publish")
    id("signing")
}

dependencyGuard {
    // Matches the variant that is published.
    configuration("releaseRuntimeClasspath")
}

android {
    namespace = "com.justeattakeaway.intervalannotatedstring"
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
            jvmTarget = JvmTarget.JVM_11
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
                name = "MavenCentral"
                url = uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")

                credentials {
                    username = project.findProperty("publish.repo.credentials.username")?.toString()
                    password = project.findProperty("publish.repo.credentials.password")?.toString()
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

                developers {
                    developer {
                        id = "justeattakeaway"
                        name = "Just Eat Takeaway.com"
                        url = "https://github.com/justeattakeaway"
                    }

                    developer {
                        id = "st235"
                        name = "Alex Dadukin"
                        url = "https://github.com/st235"
                    }

                    developer {
                        id = "mezpahlan"
                        name = "Mez Pahlan"
                        url = "https://github.com/mezpahlan"
                    }

                    developer {
                        id = "jordicollmarin"
                        name = "Jordi Coll Marin"
                        url = "https://github.com/jordicollmarin"
                    }
                }

                scm {
                    url = "https://github.com/justeattakeaway/IntervalAnnotatedString"
                    connection = "https://github.com/justeattakeaway/IntervalAnnotatedString.git"
                    developerConnection = "https://github.com/justeattakeaway/IntervalAnnotatedString.git"
                }
            }

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

signing {
    isRequired = false
    sign(publishing.publications)

    val inMemoryKey = project.findProperty("signingInMemoryKey")?.toString()
    if (inMemoryKey != null) {
        val inMemoryKeyId = project.findProperty("signingInMemoryKeyId")?.toString()
        val inMemoryKeyPassword = project.findProperty("signingInMemoryKeyPassword")?.toString()
        useInMemoryPgpKeys(inMemoryKeyId, inMemoryKey, inMemoryKeyPassword)
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
