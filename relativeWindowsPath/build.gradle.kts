plugins {
  kotlin("jvm").version("1.4.0")
  id("com.apollographql.apollo").version("2.4.1-SNAPSHOT")
}

repositories {
  mavenCentral()
  mavenLocal()
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
  service("service") {
    sourceFolder.set("com/library")
    schemaPath.set("../../../schema.sdl")
  }
}