plugins {
  id("com.android.library").version("4.0.0")
  kotlin("android").version("1.3.72")
  id("com.apollographql.apollo").version("2.2.3")
}

repositories {
  mavenCentral()
}

android {
  compileSdkVersion(30)
}
dependencies {
  implementation(kotlin("stdlib"))
  testImplementation(kotlin("test-junit"))
}