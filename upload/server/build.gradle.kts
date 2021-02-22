plugins {
  kotlin("jvm")
  kotlin("plugin.spring")
  id("application")
}

dependencies {
  api("com.expediagroup:graphql-kotlin-spring-server:3.7.0")
  implementation("com.apollographql.apollo3:apollo-gradle-plugin:${properties.get("apolloVersion")}")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.4.2")
}

application {
  mainClass.set("server.MainKt")
}