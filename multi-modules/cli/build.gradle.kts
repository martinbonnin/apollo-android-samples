plugins {
  kotlin("jvm")
  id("com.apollographql.apollo")
}

val apollo by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("com.apollographql.apollo:apollo-api:2.2.3-SNAPSHOT")
  testImplementation(kotlin("test-junit"))
  add("apollo", project(mapOf(
      "path" to ":lib",
      "configuration" to "apolloProducer"
  )))
}

tasks.register("hello") {
  doLast {
    configurations.named("apollo").get().files.let {
      it.forEach {
        println("it = ${it.readText()}")
      }
    }
  }
}
