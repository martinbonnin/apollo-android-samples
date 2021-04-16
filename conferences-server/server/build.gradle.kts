plugins {
  kotlin("jvm").version("1.4.31")
  kotlin("plugin.spring").version("1.4.31")
  id("org.springframework.boot").version("2.4.1")
  id("net.mbonnin.one.eight").version("0.1")
  id("com.google.cloud.tools.appengine").version("2.4.1")
//  id("war")
}


dependencies {
  api("com.expediagroup:graphql-kotlin-spring-server:3.6.2")
}

appengine {
  deploy {
    projectId = "android-conferences"
    version = "GCLOUD_CONFIG"
  }
}