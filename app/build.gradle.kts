plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "za.co.varsitycollage.st10050487.knights"
    compileSdk = 34

    defaultConfig {
        applicationId = "za.co.varsitycollage.st10050487.knights"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation("org.mindrot:jbcrypt:0.4")
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Responsive Design for the Images/views
    implementation("com.intuit.sdp:sdp-android:1.1.1")

    implementation("com.google.android.material:material:1.9.0")

    //Resposive Design for the text view
    implementation("com.intuit.ssp:ssp-android:1.1.1")

    // Updated Kotlin runtime dependency with actual version number
    implementation("org.jetbrains.kotlin:kotlin-script-runtime:1.7.10")

    // BCrypt dependency for password hashing
    implementation("org.mindrot:jbcrypt:0.4")

    //implement the dependency com.intuit.sdp:sdp-android:1.1.1
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    //implement the dependency com.intuit.ssp:ssp-android:1.1.1
    implementation("com.intuit.ssp:ssp-android:1.1.1")
    //Implementing the de.hdodenhof:circleimageview:3.1.0
    implementation(libs.circleimageview)



   implementation("net.zetetic:android-database-sqlcipher:4.5.4")
    implementation("androidx.sqlite:sqlite:2.4.0")
}
