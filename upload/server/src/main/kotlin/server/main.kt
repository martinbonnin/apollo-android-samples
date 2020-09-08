package server

import com.apollographql.apollo.gradle.internal.SchemaDownloader
import org.springframework.boot.runApplication
import java.io.File

fun main(args: Array<String>) {
  val context = runApplication<ServerApplication>(*args)

  val file = File("../client/src/main/graphql/com/example/schema.json")
  println("absolutePath: ${file.absolutePath}")
  SchemaDownloader.download(
      "http://localhost:8080/graphql",
      file,
      emptyMap(),
      10,
      10
  )

  context.close()
}

