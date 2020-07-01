plugins {
    id("com.android.application")
    kotlin("android")
}


java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("androidx.appcompat:appcompat:1.1.0")

    // Change here to test against 1.4 vs 2.2.1
    //implementation(project(":cache-1-4"))
    implementation(project(":cache-2-x"))

    implementation("junit:junit:4.13")
}


android {
    defaultConfig {
        targetSdkVersion(27)
        minSdkVersion(20)
        compileSdkVersion(29)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}