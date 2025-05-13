plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.weatherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.weatherapp"
        minSdk = 24
        targetSdk = 35
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(platform(libs.firebase.bom)) // latest BOM
    implementation(libs.com.google.firebase.firebase.auth.ktx)

    // Koin core features
    implementation("io.insert-koin:koin-android:3.5.3")

// Koin for Jetpack Compose
    implementation("io.insert-koin:koin-androidx-compose:3.5.3")



    implementation("io.ktor:ktor-client-core:2.3.4")
    implementation("io.ktor:ktor-client-android:2.3.1")  // Android specific engine
    implementation("io.ktor:ktor-client-serialization:2.3.1")

    // Ktor client with OkHttp engine
    implementation("io.ktor:ktor-client-okhttp:2.3.1")  // Make sure to use the correct version

    // Other Ktor dependencies (e.g., for ContentNegotiation)
    implementation("io.ktor:ktor-client-core:2.3.4")
    implementation("io.ktor:ktor-client-serialization:2.3.1") // For JSON serialization
    implementation("io.ktor:ktor-client-content-negotiation:2.3.4") // For Content Negotiation feature
    // If you're using Kotlinx Serialization
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")
    implementation ("com.google.android.gms:play-services-location:21.3.0")

    implementation("androidx.navigation:navigation-compose:2.9.0")

}