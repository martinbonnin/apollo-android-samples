plugins {
  id("org.jetbrains.kotlin.jvm").version("1.6.10")
  id("com.apollographql.apollo").version("2.5.11")
}


dependencies{
  implementation("com.apollographql.apollo:apollo-runtime:2.5.11")
  implementation("com.apollographql.apollo:apollo-coroutines-support:2.5.11")
  testImplementation("junit:junit:4.13.2")
  testImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")
}

repositories {
  mavenCentral()
}

apollo {
  generateKotlinModels.set(true)
}