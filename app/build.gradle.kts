plugins {
    alias(libs.plugins.androidApplication)
    //id("com.android.application")
    id("com.google.gms.google-services")//Google services Gradle plugin
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

        javaCompileOptions {
            annotationProcessorOptions {
                arguments(
                        mapOf("room.schemaLocation" to "$projectDir/schemas")
                )
            }
        }
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
    implementation(libs.gson)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.github.AnyChart:AnyChart-Android:1.1.5")
    implementation(libs.tensorflow.lite)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)
    implementation (libs.material)
    implementation (libs.appcompat)

    //roomDB 관련
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    //Firebase 연결을 위한 코드
    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))
    implementation("com.google.firebase:firebase-database")
    implementation(libs.firebase.auth)


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

    //chatbot 관련 라이브러리
    // OkHttp 라이브러리
    implementation(libs.okhttp)

    // JSON 처리 라이브러리 (필요 시)
    implementation(libs.json) // JSON 객체를 쉽게 처리하기 위한 라이브러리

    // AndroidX Fragment
    implementation(libs.fragment) // Fragment 라이브러리 (최신 버전으로 업데이트하세요)


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
}