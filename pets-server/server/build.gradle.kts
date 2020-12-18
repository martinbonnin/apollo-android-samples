plugins {
  kotlin("jvm")
  kotlin("plugin.spring")
  id("application")
}

dependencies {
  api("com.expediagroup:graphql-kotlin-spring-server:3.6.2")
  implementation("com.apollographql.apollo:apollo-gradle-plugin:2.4.1")
  implementation("com.squareup.okio:okio:2.9.0")
  implementation("org.antlr:antlr4:4.7.2")
}

application {
  mainClass.set("server.MainKt")
}