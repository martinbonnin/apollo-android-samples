plugins {
  id("com.android.library").version("4.0.0")
  kotlin("android").version("1.3.72")
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
  testImplementation(kotlin("test-junit"))
  implementation("com.apollographql.apollo:apollo-api:2.2.3")
}

apollo {
  generateKotlinModels.set(true)
}