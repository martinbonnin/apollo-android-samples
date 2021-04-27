import com.apollographql.apollo.gradle.api.ApolloAttributes.APOLLO_VARIANT_ATTRIBUTE

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
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

class Rule2 : AttributeCompatibilityRule<com.apollographql.apollo.gradle.api.ApolloAttributes.Variant> {

  override fun execute(details: CompatibilityCheckDetails<com.apollographql.apollo.gradle.api.ApolloAttributes.Variant>) {
    println("lib: producerValue=${details.producerValue?.name} consumerValue=${details.consumerValue?.name}")
    if (details.producerValue?.name == "main" || details.producerValue?.name == "release") {
      println("compatible")
      details.compatible()
    }
  }
}
dependencies {
  attributesSchema.attribute(com.apollographql.apollo.gradle.api.ApolloAttributes.APOLLO_VARIANT_ATTRIBUTE) {
    compatibilityRules.add(Rule2::class.java)
  }
}


android {
  setCompileSdkVersion(30)
}