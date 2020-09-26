package server

import com.apollographql.apollo.compiler.parser.introspection.IntrospectionSchema
//import com.apollographql.apollo.compiler.parser.introspection.toSDL
import com.apollographql.apollo.gradle.internal.SchemaDownloader
import org.springframework.boot.runApplication
import java.io.File

fun main(args: Array<String>) {
  val context = runApplication<ServerApplication>(*args)

  val jsonFile = File("../client/src/main/graphql/com/example/schema.json")
  val sdlFile = File("../client/src/main/graphql/com/example/schema.sdl")
  println("absolutePath: ${jsonFile.absolutePath}")
  SchemaDownloader.download(
      "http://localhost:8080/graphql",
      jsonFile,
      emptyMap(),
      10,
      10
  )
  //IntrospectionSchema(jsonFile).toSDL(sdlFile)
  jsonFile.delete()

  context.close()
}

