plugins {
  id("org.jetbrains.kotlin.jvm").version("1.5.30")
  id("com.apollographql.apollo").version("2.5.9")
}

repositories {
  mavenCentral()
}

dependencies {
  api("com.apollographql.apollo:apollo-runtime:2.5.9")
  api("com.apollographql.apollo:apollo-coroutines-support:2.5.9")
  api("com.squareup.okhttp3:logging-interceptor:4.9.1")
  implementation("junit:junit:4.13.1")
}