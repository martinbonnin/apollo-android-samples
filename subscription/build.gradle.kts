plugins {
    kotlin("jvm").version("1.3.72")
    kotlin("plugin.spring").version("1.3.72")
    id("org.springframework.boot").version("2.3.0.RELEASE")
    id("com.apollographql.apollo").version("2.2.1")
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

dependencies {
    implementation("com.expediagroup:graphql-kotlin-spring-server:3.2.0")
    implementation("org.springframework:spring-context:5.2.7.RELEASE")
    implementation("io.projectreactor:reactor-core:3.3.5.RELEASE")
    implementation("com.apollographql.apollo:apollo-runtime:2.2.1")
    implementation("com.apollographql.apollo:apollo-coroutines-support:2.2.1")


    implementation("junit:junit:4.13")
}

apollo {
    //customTypeMapping.set(mapOf("JSONObject" to "client.JsonObject"))
    generateKotlinModels.set(true)
}