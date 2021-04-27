import com.apollographql.apollo.gradle.api.ApolloAttributes

plugins {
  kotlin("jvm").version("1.4.31").apply(false)
  id("com.apollographql.apollo").version("2.5.5").apply(false)
  id("com.android.application").version("4.1.3").apply(false)
}

allprojects {
  repositories {
    mavenCentral()
    mavenLocal()
    google()
  }

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "1.8"
    }
  }
}
