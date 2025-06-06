plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
}

android {
    namespace = "com.hd.minitinder"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.hd.minitinder"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation(platform("androidx.compose:compose-bom:2025.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.stripe:stripe-android:21.11.1")
    implementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("androidx.compose.animation:animation-core-lint:1.8.0-beta02")
    implementation("com.google.android.apps.common.testing.accessibility.framework:accessibility-test-framework:4.1.1")
    implementation("androidx.security:security-crypto:1.0.0")
    implementation("org.jetbrains:annotations:15.0")
    implementation("com.google.dagger:hilt-android:2.50")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
//    androidTestImplementation(platform("androidx.compose:compose-bom:2024.04.01"))

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("com.google.firebase:firebase-analytics")

    // Firebase Auth
    implementation("com.google.firebase:firebase-auth")


    // Navigation
    val nav_version = "2.8.7"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // Facebook Login
    implementation("com.facebook.android:facebook-android-sdk:latest.release")

    //
    implementation("io.coil-kt:coil-compose:2.5.0")

    // firebase cloud firestore
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Thư viện cho các icon
    implementation("androidx.compose.material:material-icons-extended")

//    implementation("com.google.accompanist:accompanist-flowlayout")

    // firebase messaging
    implementation("com.google.firebase:firebase-messaging-ktx:24.1.0")

    // cloudinary
    implementation("com.cloudinary:cloudinary-android:3.0.2")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")

    // api
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")


}