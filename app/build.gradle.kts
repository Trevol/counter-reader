plugins {
    id("com.android.application")
    id("kotlin-android")
    //id("kotlinx-serialization")
    // kotlin("plugin.serialization")
}

android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "cam.tavrida.energysales"
        minSdk = 28
        targetSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_1_9
        targetCompatibility = JavaVersion.VERSION_1_9
    }
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra["compose_version"] as String
    }
    packagingOptions {
        exclude ("META-INF/licenses/**")
        exclude ("META-INF/AL2.0")
        exclude ("META-INF/LGPL2.1")
    }
}

val ktor_version = "1.5.4"

dependencies {

    implementation("androidx.core:core-ktx:1.5.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.compose.ui:ui:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.material:material:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-tooling:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.material:material-icons-extended:${rootProject.extra["compose_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.3.0-alpha08")
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha01")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha07")

    // implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.0")
    // implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.0")
    implementation("com.h2database:h2:1.4.200")
    implementation("org.jetbrains.exposed:exposed-core:${rootProject.extra["exposedVersion"]}")
    implementation("org.jetbrains.exposed:exposed-dao:${rootProject.extra["exposedVersion"]}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${rootProject.extra["exposedVersion"]}")
    implementation("org.jetbrains.exposed:exposed-java-time:${rootProject.extra["exposedVersion"]}")
    implementation("org.slf4j:slf4j-simple:1.7.30")

    // CameraX
    implementation ("androidx.camera:camera-core:1.1.0-alpha04")
    implementation ("androidx.camera:camera-camera2:1.1.0-alpha04")
    implementation ("androidx.camera:camera-lifecycle:1.1.0-alpha04")

    // CameraX View class
    implementation ("androidx.camera:camera-view:1.0.0-alpha24")

    implementation ("com.google.mlkit:barcode-scanning:16.1.2")

    //ktor-client
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation ("io.ktor:ktor-client-cio:$ktor_version")
    implementation ("io.ktor:ktor-client-serialization:$ktor_version")
    implementation ("io.ktor:ktor-client-gson:$ktor_version")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${rootProject.extra["compose_version"]}")
}