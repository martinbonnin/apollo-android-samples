package server.model

import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component

@Component
class RootQuery : Query {
  val characters = listOf(
      Human("Luke", 75.0),
      Droid("C3PO", "translator"),
  )

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

class Human(override val name: String, val mass: Double): Character, SearchResult
class Droid(override val name: String, val primaryFunction: String): Character, SearchResult

class Starship(val name: String): SearchResult
