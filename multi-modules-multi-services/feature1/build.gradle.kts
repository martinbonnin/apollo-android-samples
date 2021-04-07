plugins {
  kotlin("jvm")
  id("com.apollographql.apollo")
}

dependencies {
  implementation(project(":shared"))
  implementation("com.apollographql.apollo:apollo-api:2.5.5")
  testImplementation(kotlin("test-junit"))
  apolloMetadata(project(":shared"))
}

apollo {
  generateKotlinModels.set(true)

  service("service1") {
  }
}