import com.apollographql.apollo.gradle.api.ApolloAttributes
import org.gradle.api.internal.attributes.CompatibilityCheckResult

plugins {
  id("com.android.application")
  kotlin("android")
  id("com.apollographql.apollo")
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation(project(":lib"))
  implementation("com.apollographql.apollo:apollo-api:2.5.5")
  testImplementation(kotlin("test-junit"))
  apolloMetadata(project(":lib"))
  implementation("com.google.android.exoplayer:exoplayer:2.13.3")
}

apollo {
  generateKotlinModels.set(true)

  service("service1") {
  }
}


class ApolloCompatibilityRule : AttributeCompatibilityRule<ApolloAttributes.Variant> {
  override fun execute(details: CompatibilityCheckDetails<ApolloAttributes.Variant>) {
    // use release in case there's no matching variant
    if (details.producerValue?.name == "release") {
      details.compatible()
    }
  }
}

dependencies {
  attributesSchema.attribute(ApolloAttributes.APOLLO_VARIANT_ATTRIBUTE) {
    compatibilityRules.add(ApolloCompatibilityRule::class.java)
  }
}

android {
  setCompileSdkVersion(30)
  flavorDimensions.add("version")
  productFlavors {
    val demo by creating {
      // Assigns this product flavor to the "version" flavor dimension.
      // If you are using only one dimension, this property is optional,
      // and the plugin automatically assigns all the module's flavors to
      // that dimension.
      dimension = "version"
      applicationIdSuffix = ".demo"
      versionNameSuffix = "-demo"
    }
    val full by creating {
      dimension = "version"
      applicationIdSuffix = ".full"
      versionNameSuffix = "-full"
    }
  }
}