import com.apollographql.apollo.compiler.parser.introspection.IntrospectionSchema
import com.apollographql.apollo.compiler.parser.introspection.toSDL
import com.apollographql.apollo.gradle.internal.ApolloDownloadSchemaTask

plugins {
  kotlin("jvm").version("1.4.0")
  id("com.apollographql.apollo").version("2.5.9")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))
  testImplementation(kotlin("test-junit"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

apollo {
  useSemanticNaming.set(true)
  generateKotlinModels.set(true)
  service("myservice") {
    schemaPath.set("../schema.sdl")
    introspection {
      sourceSetName.set("main")
      endpointUrl.set("https://apollo-fullstack-tutorial.herokuapp.com/graphql")
    }
  }
}

tasks.withType(ApolloDownloadSchemaTask::class.java).configureEach {
  doLast {
    val schema = file("src/main/schema.sdl")
    IntrospectionSchema(schema).toSDL(schema)
  }
}