plugins {
  kotlin("jvm").version("1.4.0")
  id("com.apollographql.apollo")
  id("application")
  id("net.mbonnin.one.eight").version("0.1")
}

repositories {
  mavenCentral()
  mavenLocal()
}
dependencies {
  implementation(kotlin("stdlib"))
  implementation("com.apollographql.apollo:apollo-runtime:${properties.get("apolloVersion")}")
  implementation("com.apollographql.apollo:apollo-coroutines-support:${properties.get("apolloVersion")}")
  testImplementation(kotlin("test-junit"))
}

application {
  mainClassName = "MainKt"
}

apollo {
  generateKotlinModels.set(true)
}