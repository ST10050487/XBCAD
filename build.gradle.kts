// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.google.firebase.crashlytics) apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:8.7.3")
        classpath  ("com.google.gms:google-services:4.4.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.2")

}
}
