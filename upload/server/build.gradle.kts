plugins {
  kotlin("jvm")
  kotlin("plugin.spring")
  //id("org.springframework.boot")
}

dependencies {
  api("com.expediagroup:graphql-kotlin-spring-server:3.6.2")
  implementation("com.apollographql.apollo:apollo-gradle-plugin:2.3.1")
  implementation("org.antlr:antlr4:4.7.2")
}
