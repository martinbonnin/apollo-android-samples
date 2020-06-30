plugins {
    id("com.android.library")
    id("com.apollographql.apollo").version("2.2.1")
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

    implementation("com.apollographql.apollo:apollo-runtime:2.2.1")
    implementation("com.apollographql.apollo:apollo-android-support:2.2.1")
    implementation("com.apollographql.apollo:apollo-coroutines-support:2.2.1")

    implementation("junit:junit:4.13")
}

apollo {
    generateKotlinModels.set(true)
}

android {
    defaultConfig {
        targetSdkVersion(29)
        minSdkVersion(20)
        compileSdkVersion(29)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}