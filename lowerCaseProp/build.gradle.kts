buildscript {
  dependencies {
    classpath("com.squareup:kotlinpoet:1.6.0")
  }
}

plugins {
  kotlin("jvm").version("1.3.72")
  id("com.apollographql.apollo").version("2.3.1")
}

repositories {
  mavenCentral()
}
dependencies {
  implementation(kotlin("stdlib"))
  testImplementation(kotlin("test-junit"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

apollo {
  generateKotlinModels.set(true)
}