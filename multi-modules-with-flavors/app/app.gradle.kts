import com.apollographql.apollo.gradle.api.ApolloAttributes

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.apollographql.apollo")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":lib"))
    implementation("com.apollographql.apollo:apollo-api:2.5.5")
    testImplementation(kotlin("test-junit"))
    apolloMetadata(project(":lib"))
    implementation("com.google.android.exoplayer:exoplayer:2.13.3")
}

apollo {
    generateKotlinModels.set(true)

    service("service1") {
    }
}

configurations.all {
    if (name.matches(Regex("apollo.*Consumer"))) {
        attributes {
            afterEvaluate {
                attribute(ApolloAttributes.APOLLO_VARIANT_ATTRIBUTE, objects.named("main"))
            }
        }
    }
}

android {
    setCompileSdkVersion(30)
    flavorDimensions.add("version")
//  productFlavors {
//    val demo by creating {
//      // Assigns this product flavor to the "version" flavor dimension.
//      // If you are using only one dimension, this property is optional,
//      // and the plugin automatically assigns all the module's flavors to
//      // that dimension.
//      dimension =  "version"
//      applicationIdSuffix = ".demo"
//      versionNameSuffix = "-demo"
//    }
//    val full by creating {
//      dimension = "version"
//      applicationIdSuffix =  ".full"
//      versionNameSuffix = "-full"
//    }
//  }
}