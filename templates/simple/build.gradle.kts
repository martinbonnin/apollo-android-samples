plugins {
  kotlin("jvm").version("1.4.0")
  id("com.apollographql.apollo").version("3.0.0-SNAPSHOT")
}

repositories {
  mavenCentral()
  mavenLocal()
}
dependencies {
  implementation(kotlin("stdlib"))
  implementation("com.apollographql.apollo:apollo-api:3.0.0-SNAPSHOT")
  testImplementation(kotlin("test-junit"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

apollo {
}