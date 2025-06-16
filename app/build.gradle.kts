plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.dblocal"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.dblocal"
        minSdk = 28
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Room
    val room_version ="2.7.1"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    //Otros
    implementation("androidx.compose.material3:material3:1.3.2")
    //Grafico de circulo
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // —— Kotlin Coroutines ——
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //Live data
    val lifecycle_version = "2.9.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    //live data Compose
    implementation ("androidx.compose.runtime:runtime-livedata:1.6.8") // O la versión más reciente compatible
    //navegacion
    implementation("androidx.navigation:navigation-compose:2.7.7")
    //Fechas
    implementation(kotlin("stdlib-jdk8"))

    //Paginación
    val paging_version = "3.3.6"
    implementation("androidx.paging:paging-runtime-ktx:$paging_version") // Usamos -ktx para las extensiones de Kotlin
    implementation("androidx.paging:paging-compose:$paging_version")
    implementation ("androidx.room:room-paging:$room_version")

}