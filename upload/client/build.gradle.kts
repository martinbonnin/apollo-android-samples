plugins {
  kotlin("jvm")
  id("com.apollographql.apollo3")
}

dependencies {
  implementation("com.apollographql.apollo3:apollo-runtime:${properties.get("apolloVersion")}")
  implementation("com.apollographql.apollo3:apollo-coroutines-support:${properties.get("apolloVersion")}")
  implementation("com.apollographql.apollo3:apollo-normalized-cache-sqlite:${properties.get("apolloVersion")}")

  implementation("org.jetbrains.kotlin:kotlin-test")
  implementation("com.squareup.okhttp3:logging-interceptor:4.8.1")
  implementation("junit:junit:4.13")
  implementation(project(":server"))
}


apollo {
  customScalarsMapping.set(mapOf("Upload" to  "com.apollographql.apollo3.api.Upload"))
}