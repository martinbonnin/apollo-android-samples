plugins {
  id("com.android.application").version("4.2.0-alpha05")
  kotlin("android").version("1.3.72")
  kotlin("kapt").version("1.3.72")
  id("com.apollographql.apollo").version("2.2.3")
}

repositories {
  mavenCentral()
  jcenter()
  google()
}

android {
  compileSdkVersion(30)
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  implementation(kotlin("stdlib"))
  kapt("com.squareup.moshi:moshi-kotlin-codegen:1.9.3")
  testImplementation(kotlin("test-junit"))
  implementation("com.squareup.moshi:moshi:1.9.3")
  implementation("com.apollographql.apollo:apollo-api:2.2.3")
}

apollo {
  generateKotlinModels.set(true)
}