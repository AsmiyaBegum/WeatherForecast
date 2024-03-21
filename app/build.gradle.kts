import java.io.FileReader
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.ab.weatherforecast"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ab.weatherforecast"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties()
        properties.load(FileReader("local.properties"))
        val apiKey = properties.getProperty("api_key")
        val weatherBaseUrl = properties.getProperty("weather_base_url")
        val addressBaseUrl = properties.getProperty("address_base_url")
        buildConfigField("String", "API_KEY", apiKey)
        buildConfigField("String","WEATHER_BASE_URL",weatherBaseUrl)
        buildConfigField("String","ADDRESS_BASE_URL",addressBaseUrl)
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.osmdroid.android)
    implementation(libs.play.services.location)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("com.google.dagger:hilt-android:2.48.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
//
    ksp("com.google.dagger:dagger-compiler:2.48.1")
    ksp("com.google.dagger:hilt-android-compiler:2.48.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.6.1")

    // LiveData
    implementation(libs.androidx.livedata)
    api(libs.androidx.livedata)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.runtime.livedata)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.coroutine.android)
    implementation(libs.retrofit.coroutine.core)
    implementation(libs.retrofit.gson.converter)

    // Google Font
    implementation(libs.androidx.ui.text.google.fonts)



}