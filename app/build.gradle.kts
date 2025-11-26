plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.spotfix"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.spotfix"
        minSdk = 24
        targetSdk = 36
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

    dependencies {
        // 🔹 Firebase BOM (manages versions)
        implementation(platform("com.google.firebase:firebase-bom:34.6.0"))

        // 🔹 Firebase Auth (for Phone/OTP)
        implementation("com.google.firebase:firebase-auth")

        implementation("com.google.firebase:firebase-messaging")


        // Core Android and Kotlin libraries
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)

        // Jetpack Compose dependencies
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)

        // Compose Activity and Navigation
        implementation(libs.androidx.activity.compose)
        implementation("androidx.navigation:navigation-compose:2.7.7")

        // Google Maps dependencies for Compose
        implementation("com.google.maps.android:maps-compose:4.3.2")
        implementation("com.google.android.gms:play-services-maps:18.1.0")
        implementation("com.google.android.gms:play-services-location:21.0.1")

        // Image loading with Coil
        implementation("io.coil-kt:coil-compose:2.5.0")

        // Material Icons
        implementation("androidx.compose.material:material-icons-extended")

        // Testing dependencies
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)
            // ... other dependencies
        implementation ("com.google.accompanist:accompanist-systemuicontroller:0.28.0")
        implementation("androidx.compose.material3:material3:<version>")

    }
}
dependencies {
    implementation(libs.firebase.messaging.ktx)
}
