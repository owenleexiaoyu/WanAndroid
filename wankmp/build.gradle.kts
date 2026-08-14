plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.compose") version "1.6.0"
    kotlin("plugin.serialization") version "1.9.22"
//    id("org.jetbrains.kotlin.plugin.compose") version "1.9.20"
}

kotlin {

// Target declarations - add or remove as needed below. These define
// which platforms this KMP module supports.
// See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "cc.lixiaoyu.wanandroid.kmp"
        compileSdk = 35
        minSdk = 21

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

// For iOS targets, this is also where you should
// configure native binary output. For more information, see:
// https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#xcframeworks

// A step-by-step guide on how to include this library in an XCode
// project can be found here:
// https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "WanKMPKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
            isStatic = true
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
            isStatic = true
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
            isStatic = true
        }
    }

// Source set declarations.
// Declaring a target automatically creates a source set with the same name. By default, the
// Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
// common to share sources between related targets.
// See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
                // Add KMP dependencies here
                implementation("org.jetbrains.compose.runtime:runtime:1.6.0")
                implementation("org.jetbrains.compose.foundation:foundation:1.6.0")
                implementation("org.jetbrains.compose.material:material:1.6.0")
                implementation("org.jetbrains.compose.material:material-icons-extended:1.6.0")
                implementation("org.jetbrains.compose.ui:ui:1.6.0")
                implementation("org.jetbrains.compose.components:components-ui-tooling-preview:1.6.0")
                implementation("org.jetbrains.compose.components:components-resources:1.6.0")
                // Ktor (Kotlin 1.9.x — Ktor 2.3 line)
                val ktor = "2.3.12"
                implementation("io.ktor:ktor-client-core:$ktor")
                implementation("io.ktor:ktor-client-mock:$ktor")
                implementation("io.ktor:ktor-client-content-negotiation:$ktor")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            }
        }

        commonTest {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test:1.9.20")
            }
        }

        androidMain {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:2.3.12")
                implementation("androidx.fragment:fragment-ktx:1.6.2")
                implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
                implementation("io.coil-kt:coil-compose:2.6.0")
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation("androidx.test:runner:1.5.2")
                implementation("androidx.test:core:1.5.0")
                implementation("androidx.test.ext:junit:1.1.5")
            }
        }

        iosMain {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.12")
            }
        }
    }

}
