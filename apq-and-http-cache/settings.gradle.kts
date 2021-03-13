pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
  }

  resolutionStrategy {
    eachPlugin {
      if (requested.id.id == "com.apollographql.apollo") {
        useVersion(extra.properties.get("apolloVersion") as String)
      }
    }
  }
}

