include(":lib", ":app")

project(":lib").buildFileName = "lib.gradle"
project(":app").buildFileName = "app.gradle.kts"

pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
    google()
  }

  this.resolutionStrategy {
    eachPlugin {
      if (requested.id.id == "com.android.application") {
        this.useModule("com.android.tools.build:gradle:${requested.version}")
      }
    }
  }
}