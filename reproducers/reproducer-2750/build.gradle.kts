plugins {
  kotlin("jvm").version("1.4.10")
  id("com.apollographql.apollo").version("2.4.5")
  id("application")
  id("net.mbonnin.one.eight").version("0.1")
}

repositories {
  mavenCentral()
}

application {
  mainClassName = "MainKt"
}

dependencies {
  implementation("com.apollographql.apollo:apollo-runtime:2.4.5")
  implementation("com.apollographql.apollo:apollo-coroutines-support:2.4.5")
}


