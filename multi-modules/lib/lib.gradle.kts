plugins {
  kotlin("jvm")
  id("com.apollographql.apollo")
  id("maven-publish")
}

dependencies {
  implementation(kotlin("stdlib"))
  testImplementation(kotlin("test-junit"))
  implementation("com.apollographql.apollo:apollo-api:2.2.4-SNAPSHOT")
}

apollo {
  generateKotlinModels.set(true)
  generateApolloMetadata.set(true)
  customTypeMapping.set(mapOf("Date" to "java.util.Date"))
}
