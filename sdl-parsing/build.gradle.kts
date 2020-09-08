plugins {
  kotlin("jvm").version("1.4.0")
  id("com.apollographql.apollo").version("2.3.2-SNAPSHOT")
}

repositories {
  mavenLocal()
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