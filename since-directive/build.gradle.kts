plugins {
  kotlin("jvm").version("1.5.0")
  id("com.apollographql.apollo3")
  id("net.mbonnin.one.eight").version("0.1")
}

repositories {
  mavenCentral()
  mavenLocal()
}
dependencies {
  implementation("com.apollographql.apollo3:apollo-runtime-kotlin:${properties.get("apolloVersion")}")
  implementation("com.apollographql.apollo3:apollo-ast:${properties.get("apolloVersion")}")
  testImplementation(kotlin("test-junit"))
}

