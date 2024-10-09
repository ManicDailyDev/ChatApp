plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.facebookclone"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.facebookclone"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material.v140)

    // FIREBASE
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics-ktx") // Use the ktx version for better Kotlin support
    implementation("com.google.firebase:firebase-auth-ktx") // KTX version is preferred
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.firebaseui:firebase-ui-database:8.0.1") // Make sure this is compatible with the firebase-ui version
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Ensure the latest version for Firebase Auth
    implementation("com.google.firebase:firebase-auth:21.1.0")

    // DESIGN
    implementation(libs.glide)
    implementation(libs.glide.compiler)
    implementation(libs.androidx.exifinterface)
    implementation(libs.play.services.auth) // Ensure the Play services are up to date
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)

    // TESTS
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
