plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.alea"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.alea"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Axel Dependencies
    implementation("cz.adaptech.tesseract4android:tesseract4android:4.8.0")

    // Princce Dependencies
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("com.google.android.material:material:1.6.0")

    implementation ("androidx.activity:activity-ktx:1.7.2")
    implementation ("androidx.core:core-ktx:1.10.1")
    implementation ("androidx.appcompat:appcompat:1.6.1")

    implementation ("androidx.camera:camera-core:1.4.0-alpha01")
    implementation ("androidx.camera:camera-view:1.4.0-alpha01")
    implementation ("androidx.camera:camera-lifecycle:1.4.0-alpha01")

    implementation ("androidx.camera:camera-core:1.4.0-alpha01")
    implementation ("androidx.camera:camera-camera2:1.4.0-alpha01")
    implementation ("androidx.camera:camera-lifecycle:1.4.0-alpha01")
    implementation ("androidx.camera:camera-view:1.4.0-alpha01")

    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.airbnb.android:lottie:5.2.0")
    implementation ("com.airbnb.android:lottie:6.0.0")

    implementation ("androidx.appcompat:appcompat:1.6.1")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Nuevas dependencias para la API
//    implementation ("com.squareup.okhttp3:okhttp:4.11.0") // OkHttp para solicitudes HTTP
//    implementation ("org.json:json:20210307") // Manejo de JSON
    implementation (libs.okhttp) // OkHttp para solicitudes HTTP
    implementation (libs.json) // Manejo de JSON
}