plugins {
  kotlin("jvm").version("1.3.72")
  id("com.apollographql.apollo").version("2.2.4-SNAPSHOT")
}

repositories {
  mavenCentral()
  mavenLocal()
}
dependencies {
  implementation(kotlin("stdlib"))
  implementation("com.apollographql.apollo:apollo-runtime:2.2.4-SNAPSHOT")
  testImplementation(kotlin("test-junit"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}