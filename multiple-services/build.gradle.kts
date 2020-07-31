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

  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

