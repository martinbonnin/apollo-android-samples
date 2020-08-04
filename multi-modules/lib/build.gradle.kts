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

val apolloProducer by configurations.creating {
  isCanBeConsumed = true
}

artifacts {
  add("apolloProducer", file("hello"))
}