plugins {
  kotlin("jvm").version("1.4.0").apply(false)
  id("com.apollographql.apollo").version("2.3.1").apply(false)
  kotlin("plugin.spring").version("1.4.0").apply(false)
  id("org.springframework.boot").version("2.3.3.RELEASE").apply(false)
}

subprojects {
  repositories {
    mavenCentral()
  }

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "1.8"
    }
  }
}

