plugins {
  kotlin("jvm")
  id("com.apollographql.apollo")
  id("maven-publish")
}

dependencies {
  implementation(kotlin("stdlib"))
  testImplementation(kotlin("test-junit"))
  implementation("com.apollographql.apollo:apollo-api:2.5.5")
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

configurations.all {
  if (name.matches(Regex("apollo.*Consumer"))) {
    attributes {
      afterEvaluate {
        attribute(com.apollographql.apollo.gradle.api.ApolloAttributes.APOLLO_VARIANT_ATTRIBUTE, objects.named("main"))
      }
    }
  }
}