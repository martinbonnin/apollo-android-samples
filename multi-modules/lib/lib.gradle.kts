plugins {
  kotlin("jvm")
  id("com.apollographql.apollo")
  id("maven-publish")
}

dependencies {
  implementation(kotlin("stdlib"))
  testImplementation(kotlin("test-junit"))
  implementation("com.apollographql.apollo:apollo-api:2.2.4-SNAPSHOT")
}

abstract class GenerateSchemaTask: org.gradle.api.DefaultTask() {
  @get:Input
  abstract val times: Property<Int>

  @get:OutputFile
  abstract val outputFile: RegularFileProperty

  @org.gradle.api.tasks.TaskAction
  fun taskAction() {

    val inputTypes42 = """
input User42 {
  id: ID!
  name: String!
}

input Body42 {
  subject: String!
  content: String!
}

input MessageInput42 {
  from: User42!
  to: User42!
  body: Body42!
  encoding: Encoding!
}
"""

    val roots = """
  type Query {
      random: String!
  }

  enum Encoding {
      UTF8,
      CP1952
  }

  type Mutation {
""".trimIndent()

    val t = times.get()

    val output = outputFile.get().asFile.outputStream().bufferedWriter()
    repeat(t) {
      output.write(inputTypes42.replace("42", it.toString()))
    }
    output.write(roots)
    repeat(t) {
      output.write("sendMessage$it(\$input: MessageInput$it): String!")
    }
    output.write("}\n")
    output.flush()
    output.close()
  }
}

val generateSchemaTask = tasks.register("generateSchema", GenerateSchemaTask::class.java) {
  times.set(300)
  outputFile.set(project.layout.projectDirectory.file("src/main/graphql/schema.sdl"))
}

apollo {
  generateKotlinModels.set(true)
  generateApolloMetadata.set(true)
  customTypeMapping.set(mapOf("Date" to "java.util.Date"))
  alwaysGenerateTypesMatching.set(emptySet())
  alwaysGenerateTypesMatching.add("MessageInput0")


  onCompilationUnit {
    schemaFile.set(generateSchemaTask.flatMap { it.outputFile })
    graphqlSourceDirectorySet.srcDir("src/main/graphql/")
    graphqlSourceDirectorySet.include("**.graphql")
  }
}
