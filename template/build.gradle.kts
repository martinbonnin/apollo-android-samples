plugins {
  kotlin("jvm").version("1.3.72")
  id("com.apollographql.apollo").version("2.2.3")
}

repositories {
  mavenCentral()
}
dependencies {
  implementation(kotlin("stdlib"))
  testImplementation(kotlin("test-junit"))
}