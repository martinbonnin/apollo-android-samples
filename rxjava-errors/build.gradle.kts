plugins {
  id("org.jetbrains.kotlin.jvm").version("1.3.72")
  id("com.apollographql.apollo").version("2.2.4-SNAPSHOT")
}

repositories {
  mavenCentral()
  mavenLocal()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib")
  implementation("com.apollographql.apollo:apollo-rx2-support:2.2.4-SNAPSHOT")

  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}


