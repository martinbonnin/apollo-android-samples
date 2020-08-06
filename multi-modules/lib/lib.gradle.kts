plugins {
  kotlin("jvm")
  id("com.apollographql.apollo")
  id("maven-publish")
}

dependencies {
  implementation(kotlin("stdlib"))
  testImplementation(kotlin("test-junit"))
  implementation("com.apollographql.apollo:apollo-api:2.2.3-SNAPSHOT")
}

apollo {
  generateKotlinModels.set(true)
}

val apolloIR by configurations.creating {
  isCanBeConsumed = true
  isCanBeResolved = false
  attributes {
    //attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class.java, "apollo-ir"))
  }
}

val task = tasks.register("generateHello") {
  outputs.files(file("hello"))

  doLast {
    file("hello").writeText("Hello from generateHello")
  }
}
artifacts {
  add("apolloIR", file("hello")) {
    builtBy(task)
  }
}