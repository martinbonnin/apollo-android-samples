plugins {
  id("org.jetbrains.kotlin.jvm").version("1.3.72")
  id("com.apollographql.apollo").version("2.2.3")
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

apollo {

  service("github") {
    sourceFolder.set("com/github")
    rootPackageName.set("com.github")
  }

  service("fullstack") {
    sourceFolder.set("com/apollographql/fullstack")
    rootPackageName.set("com.apollographql.fullstack")
  }
}