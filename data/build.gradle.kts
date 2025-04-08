plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")

}

android {
    namespace = "com.korniykom.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt") ,
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
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.androidx.room.room.compiler)

    implementation(libs.ktor.ktor.client.core)
    implementation(libs.ktor.client.android.v236)
    implementation(libs.ktor.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json.v236)

    implementation(libs.dagger.hilt.android)
    ksp(libs.google.hilt.android.compiler)

    implementation(libs.dagger.hilt.android)
    ksp(libs.google.hilt.android.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}