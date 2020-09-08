pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenLocal()
    mavenCentral()
  }
}
val newData = data.copy(
    user = data.user?.copy (
        fragments = data.user?.fragments?.copy {
          userFields = school
        }
    )
)