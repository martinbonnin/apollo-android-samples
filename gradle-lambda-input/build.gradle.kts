plugins {
  kotlin("jvm").version("1.3.72")
  id("com.apollographql.apollo").version("2.2.2")
}

repositories {
  mavenCentral()
}
println("Hello")

val userName = "Martin2"

tasks.register("greeting") {
  inputs.property("name", userName)

  val outputFile = project.file("greeting")
  outputs.file(outputFile)

  doLast {
    outputFile.writeText("Hello $userName")
  }
}

tasks.register("greetingBuildSrc", BuildSrcTask::class.java) {
  username.set(project.provider { "a" })
  outputFile.set(project.file("greetingBuildSrc"))
}

val holder = project.file("producerOutput")

val producer = tasks.register("producer", Producer::class.java) {
  outputFile.set(holder)
}

tasks.register("consumer", Consumer::class.java) {
  inputFile.set(producer.map { it.outputFile.get() })
}

abstract class ManifestTransformerTask: DefaultTask() {

  @get:InputFile
  abstract val gitInfoFile: RegularFileProperty

  @get:InputFile
  abstract val mergedManifest: RegularFileProperty

  @get:OutputFile
  abstract val updatedManifest: RegularFileProperty

  @TaskAction
  fun taskAction() {

    val gitVersion = gitInfoFile.get().asFile.readText()
    var manifest = mergedManifest.asFile.get().readText()
    manifest = manifest.replace("android:versionCode=\"1\"", "android:versionCode=\"${gitVersion}\"")
    println("Writes to " + updatedManifest.get().asFile.getAbsolutePath())
    updatedManifest.get().asFile.writeText(manifest)
  }
}

apollo {
  onCompilationUnit {
    producer.configure {

      outputFile = operationOutputFile
    }
  }
}