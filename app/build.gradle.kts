import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

val compose_version = "1.5.4"
val compose_ui_version = "1.6.1"
val ktor_version = "2.3.8"
val kotlin_version = "1.9.20"

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(projects.openLocationsShared)

            // Maps
            implementation("com.google.android.gms:play-services-location:21.3.0")
            implementation("com.google.android.gms:play-services-location:21.3.0")
            implementation("com.google.maps.android:maps-compose:6.1.1")
            implementation("com.google.maps.android:maps-compose-utils:6.1.1")
            implementation("com.google.accompanist:accompanist-permissions:0.32.0")

            // Ads
            implementation("com.google.android.gms:play-services-ads:23.3.0")

            // Date Time
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

            // Images
            implementation("io.coil-kt:coil-compose:2.5.0")

            implementation("androidx.core:core-ktx:1.12.0")
            implementation("androidx.compose.foundation:foundation:1.6.2")
            implementation("androidx.compose.ui:ui:$compose_ui_version")
            implementation("androidx.compose.ui:ui-tooling-preview:$compose_ui_version")
            implementation("androidx.compose.material:material:$compose_ui_version")
            implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
            implementation("androidx.navigation:navigation-compose:2.7.7")
            implementation("androidx.activity:activity-compose:1.8.2")
            implementation("io.ktor:ktor-client-core:$ktor_version")
            implementation("io.ktor:ktor-client-android:$ktor_version")
            implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
            implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
            implementation("com.fredporciuncula:flow-preferences:1.9.1")
        }
    }
}

android {
    namespace = "mn.openlocations"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "mn.openlocations"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

