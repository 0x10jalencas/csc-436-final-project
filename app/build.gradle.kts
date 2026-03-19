import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

android {
    namespace = "com.example.csc_436_final_project"
    compileSdk = 35

    buildFeatures {
        compose = true
        buildConfig = true 
    }

    defaultConfig {
        applicationId = "com.example.csc_436_final_project"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val rawKey = localProperties.getProperty("GEMINI_API_KEY") ?: ""
        val cleanKey = rawKey.replace("\"", "") 
        buildConfigField("String", "GEMINI_API_KEY", "\"$cleanKey\"")
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.navigation.compose)
    implementation(platform("androidx.compose:compose-bom:2025.01.00"))
    implementation("androidx.compose.material3:material3")
    implementation(libs.kotlinx.serialization.json)
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    // Reverted to stable version
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
}
