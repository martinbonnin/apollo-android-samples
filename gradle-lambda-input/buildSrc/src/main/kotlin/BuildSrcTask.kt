import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import java.util.*

abstract class BuildSrcTask : DefaultTask() {

  @get:Input
  abstract val username: Property<String>

  @get:OutputFile
  abstract val outputFile: RegularFileProperty

  @TaskAction
  fun taskAction() {
    outputFile.asFile.get().writeText("Hello (from buildSrc) ${username.get()}")
  }
}

abstract class Producer : DefaultTask() {

  @get:OutputFile
  abstract val outputFile: RegularFileProperty

  abstract val value: Property<String>

  @TaskAction
  fun taskAction() {
    outputFile.asFile.get().writeText("Hello from producer")
  }
}

abstract class Consumer : DefaultTask() {

  @get:InputFile
  abstract val inputFile: RegularFileProperty


  @TaskAction
  fun taskAction() {
    println(inputFile.asFile.get().readText())
  }
}