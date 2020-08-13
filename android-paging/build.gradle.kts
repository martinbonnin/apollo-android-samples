plugins {
  id("com.android.application").version("4.2.0-alpha07")
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
  defaultConfig {
    applicationId = "com.paging"
    minSdkVersion(27)
    versionCode = 1
    versionName = "1.0"
  }
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
  implementation("androidx.paging:paging-runtime:3.0.0-alpha04")
  implementation("androidx.appcompat:appcompat:1.2.0")
  implementation("androidx.core:core-ktx:1.3.1")
  kapt("com.squareup.moshi:moshi-kotlin-codegen:1.9.3")
  testImplementation(kotlin("test-junit"))
  implementation("com.squareup.moshi:moshi:1.9.3")
  implementation("com.apollographql.apollo:apollo-api:2.2.3")
  implementation("com.apollographql.apollo:apollo-coroutines-support:2.2.3")
}

apollo {
  generateKotlinModels.set(true)
}