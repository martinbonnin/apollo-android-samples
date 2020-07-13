plugins {
    kotlin("jvm").version("1.3.72")
    id("com.apollographql.apollo").version("2.2.2")
}

apollo {
    generateKotlinModels.set(true)
}

repositories {
    jcenter()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

dependencies {
    implementation("com.apollographql.apollo:apollo-api:2.2.2")
}