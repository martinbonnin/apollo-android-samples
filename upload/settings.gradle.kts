include(":server")
include(":client")


pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
  }

  resolutionStrategy {
    eachPlugin {
      if (requested.id.id == "com.apollographql.apollo3") {
        useVersion(extra.properties.get("apolloVersion") as String)
      }
    }
  }
}

