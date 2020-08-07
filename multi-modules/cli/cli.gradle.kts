plugins {
  kotlin("jvm")
  id("com.apollographql.apollo")
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation(project(":lib"))
  implementation("com.apollographql.apollo:apollo-api:2.2.3-SNAPSHOT")
  testImplementation(kotlin("test-junit"))
  add("apollo", project(":lib"))
}
