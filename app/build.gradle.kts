plugins {
    id("com.android.application")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.ckcitlabroom"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.ckcitlabroom"
        minSdk = 29
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("com.onesignal:OneSignal:[5.1.6, 5.1.99]")

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.compose.material:material:1.8.2")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.material:material-icons-extended-android:1.7.8")
    implementation("androidx.navigation:navigation-compose:2.9.0")
    implementation("androidx.compose.animation:animation:1.9.0-alpha03")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.1")

    implementation("androidx.core:core-ktx:1.16.0")

    // Icons
    implementation("com.composables:icons-lucide:1.0.0")

    // Retrofit + Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")

    // Fuel
    implementation("com.github.kittinunf.fuel:fuel:2.3.1")
    implementation("com.github.kittinunf.fuel:fuel-android:2.3.1")
    implementation("com.github.kittinunf.fuel:fuel-json:2.3.1")

    // ViewModel & Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.1")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")

    // Coil
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    // DataStore
    implementation("androidx.datastore:datastore:1.1.7")
    implementation("androidx.datastore:datastore-preferences:1.1.7")

    // CameraX & QR
    implementation("com.google.zxing:core:3.5.2")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("androidx.camera:camera-core:1.4.2")
    implementation("androidx.camera:camera-camera2:1.4.2")
    implementation("androidx.camera:camera-lifecycle:1.4.2")
    implementation("androidx.camera:camera-view:1.4.2")
    implementation("com.google.mlkit:barcode-scanning:17.3.0")

    // Activity Compose
    implementation("androidx.activity:activity-compose:1.10.1")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.10.1")

    // Firebase (use BOM!)
    implementation(platform("com.google.firebase:firebase-bom:33.15.0"))
    implementation("com.google.firebase:firebase-messaging:24.1.1")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging-ktx")


    implementation ("com.google.accompanist:accompanist-navigation-animation:0.34.0")

    implementation ("com.google.accompanist:accompanist-systemuicontroller:<version>")



    // Google Maps & Other Libs
    implementation(libs.play.services.maps)
    implementation(libs.common)
    implementation(libs.androidx.runtime.livedata)

    // Compose BOM again (if needed globally)
    val composeBom = platform("androidx.compose:compose-bom:2024.09.00")
    implementation(composeBom)



    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
