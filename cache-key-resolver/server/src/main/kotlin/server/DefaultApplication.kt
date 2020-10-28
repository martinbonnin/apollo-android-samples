package server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DefaultApplication {
}

fun runServer() = runApplication<DefaultApplication>()


