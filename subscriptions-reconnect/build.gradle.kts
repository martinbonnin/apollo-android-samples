plugins {
  kotlin("jvm").version("1.4.10").apply(false)
  id("com.apollographql.apollo").version("2.4.2-SNAPSHOT").apply(false)
  kotlin("plugin.spring").version("1.4.0").apply(false)
  id("org.springframework.boot").version("2.3.3.RELEASE").apply(false)
  id("net.mbonnin.one.eight").version("0.1")
}

subprojects {
  repositories {
    mavenCentral()
    mavenLocal()
  }
}

