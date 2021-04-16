package server

//import com.apollographql.apollo.compiler.parser.introspection.toSDL
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
  runApplication<DefaultApplication>(*args)


  // Don't close, one can always Ctrl-C
  // context.close()

  // go to http://localhost:8080/playground for playground
}

