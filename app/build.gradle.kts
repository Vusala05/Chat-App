plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.chatapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.chatapp"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.messaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Scalable size unit(Support for different screens size)
    implementation (libs.intuit.sdp.android)
    implementation (libs.ssp.android)


    //Rounded imageView
    implementation (libs.makeramen.roundedimageview)
    implementation (libs.hdodenhof.circleimageview)


   // implementation (libs.roundedimageview)
    //implementation (libs.circleimageview)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
    implementation (libs.firebase.analytics)


    // Jetpack Compose integration

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation (libs.androidx.navigation.compose) // Replace with your actual version
// Views/Fragments integration
    implementation (libs.androidx.navigation.fragment.ktx.v253)
    implementation (libs.androidx.navigation.ui.ktx.v253)
// Feature module support for Fragments
    implementation (libs.androidx.navigation.dynamic.features.fragment)
// Testing Navigation
    androidTestImplementation (libs.androidx.navigation.testing)
//for picasso
    implementation (libs.picasso)


}