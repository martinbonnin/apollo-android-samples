plugins {
    id("com.android.application").version("3.6.2").apply(false)
    kotlin("android").version("1.3.72").apply(false)
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
        google()
        mavenLocal()
    }
}