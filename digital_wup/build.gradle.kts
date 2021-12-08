buildscript {
  repositories {
    mavenCentral()
    google()
  }
  dependencies {
    classpath("com.android.library:com.android.library.gradle.plugin:4.2.0")
  }
}
plugins {
  id("digital.wup.android-maven-publish").version("3.6.2")
  id("com.apollographql.apollo").version("2.4.1").apply(true)
}

apply(plugin = "com.android.library")

repositories {
  mavenCentral()
  google()
}

publishing {
  publications {
    create("aar", MavenPublication::class.java) {
      from(components.named("android").get())
      groupId = "com.example"
      //artifactId "$project.ext.artifactId"
    }
  }
}

dependencies {
  add("implementation", "dev.gradleplugins:gradle-api:7.1")
}

configure<com.android.build.gradle.BaseExtension> {
  compileSdkVersion(28)

  defaultConfig {

  }
}

val producerConfigurationName = "toto"
project.configurations.create(producerConfigurationName) {
  isCanBeConsumed = true
  isCanBeResolved = false
}

//val totoFile = project.provider {
//  file("toto")
//}
//project.configurations.getByName("archives").artifacts.all {
//  println("adding $name")
//}
//project.artifacts.add(producerConfigurationName, totoFile)
//project.configurations.getByName("toto").artifacts.clear()
//project.configurations.getByName("archives").artifacts.removeAll {
//  println("removing ${it}")
//  it.name == "toto"
//}

println("done")

afterEvaluate {
  project.configurations.getByName("archives").artifacts.removeAll {
    it.name == "metadata"
  }
}
