#!/usr/bin/env kotlin

import java.io.File

val template = File("template.graphql").readText()

val output = File("src/main/graphql/queries.graphql").outputStream().bufferedWriter()
repeat(300) {
  val query = template.replace("SomeQuery", "SomeQuery$it")
  output.write(query)
}

output.flush()
output.close()