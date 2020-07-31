pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
  }
  resolutionStrategy {
    eachPlugin {
      val requestedId = requested.id.id
      when  {
        requestedId.startsWith("com.android") -> useModule("com.android.tools.build:gradle:${requested.version}")
      }
    }
  }
}