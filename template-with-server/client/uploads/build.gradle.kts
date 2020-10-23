plugins {
  kotlin("jvm")
  id("com.apollographql.apollo")
}

dependencies {
  implementation("com.apollographql.apollo:apollo-runtime:2.4.1")
  implementation("com.apollographql.apollo:apollo-coroutines-support:2.4.1")

  implementation("org.jetbrains.kotlin:kotlin-test")
  implementation("com.squareup.okhttp3:logging-interceptor:4.8.1")
  implementation("junit:junit:4.13")
  implementation(project(":server"))
}


apollo {
  customTypeMapping.set(mapOf("Upload" to  "com.apollographql.apollo.api.FileUpload"))
}