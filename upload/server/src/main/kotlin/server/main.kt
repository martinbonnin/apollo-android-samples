package server

import com.apollographql.apollo3.gradle.internal.SchemaDownloader
import org.springframework.boot.runApplication
import java.io.File

fun main(args: Array<String>) {
  runApplication<ServerApplication>(*args)

  val file = File("client/src/main/graphql/com/example/schema.json")
  println("absolutePath: ${file.absolutePath}")
  val schema = SchemaDownloader.downloadIntrospection(
      "http://localhost:8080/graphql",
      emptyMap(),
  )

  file.writeText(schema)

  while (true) {
    Thread.sleep(1000)
  }
}

