plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"
}


android {
    namespace = "be.shylo.hydratenow"
    compileSdk = 33

    defaultConfig {
        applicationId = "be.shylo.hydratenow"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
    }

    buildFeatures{
        viewBinding = true
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

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("androidx.test:core-ktx:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.airbnb.android:lottie:6.3.0")

    val room_version = "2.5.0"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    //Charts
    implementation ("com.github.AnyChart:AnyChart-Android:1.1.5")

    //Coroutines:
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")

    // Lifecycle dependencies
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")

    // Test dependencies
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("io.mockk:mockk:1.12.0")
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    testImplementation ("org.robolectric:robolectric:4.11.1")
    testImplementation ("org.mockito:mockito-core:3.8.0")
    androidTestImplementation ("com.google.truth:truth:1.0.1")
    androidTestImplementation ("android.arch.core:core-testing:1.0.0")

}