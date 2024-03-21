// Top-level build file where you can add configuration options common to all sub-projects/modules.

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        maven { url = uri("https://jitpack.io") }
    }
    dependencies {
    }
}




plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false
}
