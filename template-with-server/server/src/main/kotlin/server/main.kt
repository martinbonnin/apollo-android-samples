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
  val introspection = SchemaDownloader.downloadIntrospection(
      "http://localhost:8080/graphql",
      emptyMap(),
  )

  IntrospectionSchema(introspection).toSDL(sdlFile)
  jsonFile.delete()

  // Don't close, one can always Ctrl-C
  // context.close()
}

