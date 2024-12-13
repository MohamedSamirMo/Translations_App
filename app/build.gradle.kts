plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.GlobalTongue.translationapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.GlobalTongue.translationapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
    // Core Android libraries
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Google ML Kit Translator
    implementation("com.google.mlkit:translate:17.0.2")

    // Animation library
    implementation("com.airbnb.android:lottie:6.1.0")

    // JUnit for unit tests
    testImplementation("junit:junit:4.13.2")

    // Kotlin coroutine testing (optional if you're using coroutines)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")

    // Multidex (remove if not needed)
    implementation("androidx.multidex:multidex:2.0.1")
    testImplementation ("io.mockk:mockk:1.12.0")
    androidTestImplementation ("androidx.test.ext:junit:1.2.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.1")

    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.6.1")
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:3.6.1")
    androidTestImplementation ("androidx.test.ext:junit:1.2.1")
    testImplementation ("io.mockk:mockk:1.13.5") // Check for the latest version
    testImplementation ("net.bytebuddy:byte-buddy:1.14.4") // Check for the latest version
    testImplementation ("net.bytebuddy:byte-buddy-agent:1.14.4")
}
