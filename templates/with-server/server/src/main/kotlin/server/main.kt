package server

import com.apollographql.apollo.compiler.parser.introspection.IntrospectionSchema
import com.apollographql.apollo.compiler.parser.introspection.toSDL
//import com.apollographql.apollo.compiler.parser.introspection.toSDL
import com.apollographql.apollo.gradle.internal.SchemaDownloader
import okio.Buffer
import org.springframework.boot.runApplication
import java.io.File

fun main(args: Array<String>) {
  runApplication<ServerApplication>(*args)

  val sdlFile = File("../client/src/main/graphql/com/example/schema.sdl")
  println("absolutePath: ${sdlFile.absolutePath}")
  val introspection = SchemaDownloader.downloadIntrospection(
      "http://localhost:8080/graphql",
      emptyMap(),
  )

  IntrospectionSchema(introspection.byteInputStream(), "").toSDL(sdlFile)

  // Don't close, one can always Ctrl-C
  // context.close()

  // go to http://localhost:8080/playground for playground
}

