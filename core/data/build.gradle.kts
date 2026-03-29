plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget()

    // iOS targets
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    sourceSets {
        commonMain.dependencies {
            // DI - Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.annotations)

            // DB - Sqlite
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.androidx.annotation)

            // Network - Ktor
            implementation(libs.ktor.client.core)

            // Logging - Kermit
            implementation(libs.kermit)

            implementation(project(":core:domain"))
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.android.driver)

            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.native.driver)

            implementation(libs.ktor.client.darwin)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.koin.test)
            implementation(libs.turbine)

            implementation(libs.ktor.client.mock)
            implementation(libs.sqldelight.sqlite.driver)
        }

        androidUnitTest.dependencies {
            implementation(libs.sqlite.driver)
        }
        iosTest.dependencies {
            implementation(libs.sqlite.driver)
        }
    }
}

sqldelight {
    databases {
        create("ChatDatabase") {
            packageName.set("com.sunday.data.local") // Match location of .sq file
        }
    }
}

android {
    namespace = "com.sunday.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    val kspCompiler = libs.koin.ksp.compiler
    add("kspAndroid", kspCompiler)

    kotlin.targets
        .filter { it.targetName.startsWith("ios") }
        .forEach { add("ksp${it.targetName.replaceFirstChar { c -> c.uppercase() }}", kspCompiler) }
}