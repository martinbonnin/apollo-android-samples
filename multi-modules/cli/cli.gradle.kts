plugins {
  kotlin("jvm")
  id("com.apollographql.apollo")
  id("application")
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation(project(":lib"))
  implementation("com.apollographql.apollo:apollo-api:2.4.0")
  testImplementation(kotlin("test-junit"))
  apolloMetadata(project(":lib"))
}

application {
  mainClass.set("MainCliKt")
}

apollo {
  generateKotlinModels.set(true)
}