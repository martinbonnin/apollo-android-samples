plugins {
  kotlin("jvm").version("1.4.31").apply(false)
  id("com.apollographql.apollo").version("2.5.5").apply(false)
}

allprojects {
  repositories {
    mavenCentral()
    mavenLocal()
  }

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "1.8"
    }
  }
}

