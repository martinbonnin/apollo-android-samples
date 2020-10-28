package server.model

import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component

@Component
class RootQuery : Query {
  companion object {
    val luke = Character("0", "Luke")
    val leia = Character("1", "Leia")
    val hanSolo = Character("2", "Han Solo")

    val darkVador = Character("3", "Dark Vador")
    val emperor = Character("4", "Emperor")
  }

  fun allCharacters(): List<Character> {
    return listOf(
        luke,
        leia,
        hanSolo,
        darkVador,
        emperor
    )
  }

  class Character(val id: String, val name: String) {
    val friends: List<Character>
      get() = when (name) {
        "Luke" -> listOf(leia, hanSolo)
        "Leia" -> listOf(luke, hanSolo)
        "Han Solo" -> listOf(luke, leia)
        "Dark Vador" -> listOf(luke, emperor)
        "Emperor" -> emptyList()
        else -> emptyList()
      }

  }
}

