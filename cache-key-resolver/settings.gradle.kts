include(":server")
include(":client")


pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenLocal()
    mavenCentral()
  }

  resolutionStrategy {
    eachPlugin {
      if (requested.id.id == "com.apollographql.apollo") {
        useVersion(extra.properties.get("apolloVersion") as String)
      }
    }
  }
}

