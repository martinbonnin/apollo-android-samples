plugins {
  kotlin("jvm")
  kotlin("plugin.spring")
  id("application")
}

dependencies {
  api("com.expediagroup:graphql-kotlin-spring-server:3.6.2")
  implementation("com.apollographql.apollo3:apollo-gradle-plugin:${properties.get("apolloVersion")}")
}

application {
  mainClass.set("server.MainKt")
}