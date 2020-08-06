plugins {
  kotlin("jvm")
  id("com.apollographql.apollo")
}

val apollo2 by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
  attributes {
    attribute(Usage.USAGE_ATTRIBUTE, objects.named("apollo-ir"))
  }
}


dependencies {
  implementation(kotlin("stdlib"))
  implementation(project(":lib"))
  implementation("com.apollographql.apollo:apollo-api:2.2.3-SNAPSHOT")
  testImplementation(kotlin("test-junit"))
  add("apollo2", project(mapOf( "path" to ":lib", "configuration" to "apolloIR")))
  /*add("apollo2", project.rootProject.subprojects
      .filter { it.name.contains("lib") }
      .get(0)
      .tasks.named("apolloMetadataZip")
      .get()
      .let {
        println("Class=" + it.javaClass.canonicalName)
        (it as Zip).archiveFile
      }
  )*/
}

tasks.register("hello") {
  println("configuring hello")

  dependsOn(configurations.named("apollo2"))

  doLast {
    configurations.named("apollo2").get().resolve()
    configurations.named("apollo2").get().incoming.artifacts.let {
      it.forEach {
        println("it = ${it.file}")
      }
    }
  }
}
