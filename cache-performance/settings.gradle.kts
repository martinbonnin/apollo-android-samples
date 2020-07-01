rootProject.name="cache-performance"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        mavenCentral()
        google()
        jcenter()
    }
    resolutionStrategy {
        this.eachPlugin {
            when  {
                requested.id.id == "com.android.library" -> {
                    useModule("com.android.tools.build:gradle:${requested.version}")
                }
                requested.id.id == "com.android.application" -> {
                    useModule("com.android.tools.build:gradle:${requested.version}")
                }
            }
        }
    }
}

include(":app")
include(":cache-1-4")
include(":cache-2-x")
