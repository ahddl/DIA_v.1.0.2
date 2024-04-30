plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.dia_v102"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dia_v102"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Normal
    implementation("io.github.ParkSangGwon:tedpermission-normal:3.3.0")
    // Coroutine
    implementation("io.github.ParkSangGwon:tedpermission-coroutine:3.3.0")
    // RxJava2
    implementation("io.github.ParkSangGwon:tedpermission-rx2:3.3.0")
    // RxJava3
    implementation("io.github.ParkSangGwon:tedpermission-rx3:3.3.0")
}