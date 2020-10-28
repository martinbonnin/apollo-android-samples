plugins {
  kotlin("jvm")
  id("com.apollographql.apollo")
}

dependencies {
  implementation("com.apollographql.apollo:apollo-runtime:${properties.get("apolloVersion")}")
  implementation("com.apollographql.apollo:apollo-coroutines-support:${properties.get("apolloVersion")}")
  implementation("com.apollographql.apollo:apollo-normalized-cache-sqlite-jvm:${properties.get("apolloVersion")}")

  implementation("org.jetbrains.kotlin:kotlin-test")
  implementation("com.squareup.okhttp3:logging-interceptor:4.8.1")
  implementation("junit:junit:4.13")
  implementation(project(":server"))
}


apollo {
  generateKotlinModels.set(true)
}