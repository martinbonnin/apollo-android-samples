pluginManagement {
  repositories {
    mavenLocal()
    google()
    gradlePluginPortal()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
  }
  resolutionStrategy {
    eachPlugin {
      if (this.requested.id.id == "com.android.library") {
        useModule("com.android.tools.build:gradle:4.0.0")
      }
    }
  }
}