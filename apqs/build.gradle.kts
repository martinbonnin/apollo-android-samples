plugins {
    kotlin("jvm").version("1.3.72")
    id("com.apollographql.apollo").version("2.2.3-SNAPSHOT")
}

repositories {
    mavenCentral()
    mavenLocal()
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("com.apollographql.apollo:apollo-runtime:2.2.3-SNAPSHOT")
    implementation("com.apollographql.apollo:apollo-coroutines-support:2.2.3-SNAPSHOT")
    implementation("com.squareup.okhttp3:logging-interceptor:4.7.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")

    implementation("junit:junit:4.13")
}

apollo {
    generateKotlinModels.set(true)
}