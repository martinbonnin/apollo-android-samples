package server.model

import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component

@Component
class RootQuery : Query {

  fun animals(): List<Animal> {
    return listOf(
        Turtle(age = 120, name = "Michelangelo"),
        Dog(age = 8, temperature = 37.0, name = "Medor")
    )
  }
}

interface Animal {
  val age: Int
}

interface WarmBlooded {
  val temperature: Double
}

interface Pet {
  val name: String
}

class Turtle(override val age: Int, override val name: String) : Animal, Pet {
}

class Dog(override val age: Int, override val name: String, override val temperature: Double) : Animal, WarmBlooded, Pet {
}