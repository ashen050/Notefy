plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services) // Ensures Firebase services are linked
}

android {
    namespace = "com.example.notesync"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.notesync"
        minSdk = 24
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
}

dependencies {
    // Android core libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.room:room-runtime:2.4.0")
    implementation(libs.firebase.inappmessaging.display)
    annotationProcessor("androidx.room:room-compiler:2.4.0")
    implementation("androidx.room:room-ktx:2.4.0") // For Kotlin extensions

    // Firebase Authentication for email/password authentication
    implementation(libs.firebase.auth)

    // Biometric Authentication for fingerprint unlocking
    implementation("androidx.biometric:biometric:1.2.0-alpha05")

    // Firebase Messaging for push notifications
    implementation("com.google.firebase:firebase-messaging:23.1.0")

    // Retrofit for API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
