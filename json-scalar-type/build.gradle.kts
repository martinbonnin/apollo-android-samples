plugins {
    kotlin("jvm").version("1.3.72")
    kotlin("plugin.spring").version("1.3.72")
    id("application")
    id("org.springframework.boot").version("2.3.0.RELEASE")
    id("com.apollographql.apollo").version("2.4.5")
}

repositories {
    mavenCentral()
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

application {
    mainClassName = "server.MainKt"
}

dependencies {
    implementation("com.expediagroup:graphql-kotlin-spring-server:3.2.0")
    implementation("org.springframework:spring-context:5.2.7.RELEASE")
    implementation("com.apollographql.apollo:apollo-runtime:2.4.5")
    implementation("com.apollographql.apollo:apollo-coroutines-support:2.4.5")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    implementation("junit:junit:4.13")
}

apollo {
    //customTypeMapping.set(mapOf("JSONObject" to "client.JsonObject"))
    generateKotlinModels.set(true)
}