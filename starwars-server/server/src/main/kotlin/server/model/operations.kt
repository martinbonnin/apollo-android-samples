package server.model

import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component

val allCharacters: Map<String, Character> = mapOf(
    "Leia" to Human("Leia", "princess",
        Appearance(
            "dark blond",
            Measurements(45)
        )
    ),
    "Luke" to Human("Luke", "pilot",
        Appearance(
            "brown",
            Measurements(54)
        )
    ),
    "C3PO" to Droid("C3PO", "translator",
        Parts(
            "electric",
            Mechanical(4, 42)
        )
    ),
)

@Component
class RootQuery : Query {
  val characters = allCharacters.values.toList()

  val starships = listOf(
      Starship("FalconMillenium")
  )

  val searchResult = (characters as List<SearchResult>) + starships

  fun iAmLucky(): SearchResult {
    return searchResult.random()
  }

  fun search(query: QueryInput): SearchResult {
    return searchResult.random()
  }

  fun randomCharacter(): Character {
    return (characters as List<Character>).random()
  }
}

enum class Operator {
  EQ,
  GT,
  LT
}

class QueryInput(val text: String, val op: Operator)

interface SearchResult

interface Character {
  val name: String
}

class Measurements(val height: Int)
class Appearance(val hairColor: String, val measurements: Measurements)
class Mechanical(val springs: Int, val screws: Int)
class Parts(val supplier: String, val mechanical: Mechanical)
class FriendsConnection(val edges: List<Character>, val count: Int)
class Human(
    override val name: String,
    val job: String,
    val appearance: Appearance,
) : Character, SearchResult {
  fun friendsConnection() = FriendsConnection(listOf(allCharacters["Leia"]!!), 1)
}

class Droid(
    override val name: String,
    val primaryFunction: String,
    val parts: Parts,
) : Character, SearchResult {
  fun friendsConnection() = FriendsConnection(listOf(allCharacters["Luke"]!!), 1)
}

class Starship(val name: String) : SearchResult
