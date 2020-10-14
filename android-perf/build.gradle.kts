plugins {
  id("com.android.application").version("4.2.0-alpha13")
  kotlin("android").version("1.4.10")
  kotlin("kapt").version("1.4.10")
  id("com.apollographql.apollo").version("3.0.0-SNAPSHOT")
}

repositories {
  mavenLocal()
  mavenCentral()
  jcenter()
  google()
}

android {
  compileSdkVersion(30)
  defaultConfig {
    minSdkVersion(28)
  }
  buildTypes {
    val release by getting {
      this.minifyEnabled(true)
      this.signingConfig = signingConfigs.getByName("debug")
    }
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
  kapt("com.squareup.moshi:moshi-kotlin-codegen:1.9.3")
  testImplementation(kotlin("test-junit"))
  implementation("com.squareup.moshi:moshi:1.9.3")
  implementation("com.apollographql.apollo:apollo-api:3.0.0-SNAPSHOT")
}

apollo {
}