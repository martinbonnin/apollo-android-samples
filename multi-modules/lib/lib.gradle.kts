plugins {
  kotlin("jvm")
  id("com.apollographql.apollo")
}

dependencies {
  implementation(kotlin("stdlib"))
  testImplementation(kotlin("test-junit"))
  implementation("com.apollographql.apollo:apollo-api:2.4.0")
}

apollo {
  generateKotlinModels.set(true)
  generateApolloMetadata.set(true)
  customTypeMapping.set(mapOf("Date" to "java.util.Date"))

  service("service1") {
    schemaFile.set(file("src/main/graphql/com/library/schema.sdl"))
    sourceFolder.set("com/library")
    rootPackageName.set("sample.graphql")
  }
}
