plugins {
  id("com.android.library").version("4.0.0")
  kotlin("android").version("1.4.0")
  id("com.apollographql.apollo").version("2.4.1")
  id("net.mbonnin.one.eight").version("0.1")
  id("com.gradleup.auto.manifest").version("1.0.2")
  id("digital.wup.android-maven-publish").version("3.6.2")
}

repositories {
  mavenCentral()
  mavenLocal()
  google()
  jcenter()
}
dependencies {
  implementation(kotlin("stdlib"))
  implementation("com.apollographql.apollo:apollo-api:2.4.1")
  testImplementation(kotlin("test-junit"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

autoManifest {
  // Mandatory packageName
  packageName.set("com.company.example")
}

android {
  compileSdkVersion(30)
}
apollo {
  generateApolloMetadata.set(true)
}

afterEvaluate {
  afterEvaluate {
    publishing {
      publications {
        create("release", MavenPublication::class.java) {
          from(components.getByName("android"))
          groupId = "com.example"
          artifactId = "artifact"
          version = "1.0"
        }
      }
    }
  }
}