plugins {
  id("org.jetbrains.kotlin.jvm").version("1.5.31")
  id("org.jetbrains.kotlin.kapt").version("1.5.31")
  id("com.apollographql.apollo3").version("3.0.0-alpha07")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("com.apollographql.apollo3:apollo-api:3.0.0-alpha07")
  kapt("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")
}

apollo {
  packageName.set("com.example")
}