package server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class DefaultApplication {

  @Bean
  fun hooks() = ReportingSchemaGeneratorHooks()
}

fun runServer() = runApplication<DefaultApplication>()
