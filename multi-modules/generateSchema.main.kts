#!/usr/bin/env kotlin

import java.io.File

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

val times = args.getOrNull(0)?.toInt() ?: 10000

val output = File("lib/src/main/graphql/com/library/schema.sdl").outputStream().bufferedWriter()
repeat(times) {
  output.write(inputTypes42.replace("42", it.toString()))
}
output.write(roots)
repeat(times) {
  output.write("sendMessage$it(\$input: MessageInput$it): String!")
}
output.write("}\n")
output.flush()
output.close()