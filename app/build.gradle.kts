import java.util.Properties

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
    // id("kotlin-android") // comentar porque no uso Kotlin
}


android {
    namespace = "com.miapp.agentegamer"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.miapp.agentegamer"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        val rawgApiKey = project.findProperty("RAWG_API_KEY")?.toString() ?: throw GradleException("RAWG_API_KEY no definida")
        buildConfigField ("String", "RAWG_API_KEY", "\"$rawgApiKey\"")
    }

    buildFeatures{
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // Core
    implementation("androidx.core:core:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")

    // UI
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // Lifecycle & ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.10.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.10.0")

    // Room (persistencia)
    implementation("androidx.room:room-runtime:2.8.4")
    annotationProcessor("androidx.room:room-compiler:2.8.4")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // OkHttp (logging)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    //Worker
    implementation("androidx.work:work-runtime:2.11.0")

    //Guava (ListenableFuture)
    implementation("com.google.guava:guava:32.1.2-android")

    //Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:34.7.0"))

    //Firebase Auth
    implementation("com.google.firebase:firebase-auth")

    //Firestore (para el presupuseto del usuario)
    implementation("com.google.firebase:firebase-firestore")

    // Hilt - Inyección de Dependencias
    implementation("com.google.dagger:hilt-android:2.51.1")
    annotationProcessor("com.google.dagger:hilt-compiler:2.51.1")

    // Hilt Worker (WorkManager)
    implementation("androidx.hilt:hilt-work:1.2.0")
    annotationProcessor("androidx.hilt:hilt-compiler:1.2.0")

}

apply (plugin = "com.google.gms.google-services")

