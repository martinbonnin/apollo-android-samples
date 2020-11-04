package server.model

import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component

@Component
class RootQuery : Query {

  fun random(): SearchResult {
    return listOf(
        Human("Luke", 75.0),
        Droid("C3PO", "translator"),
        Starship("FalconMillenium")
    ).random()
  }

  fun search(query: QueryInput): SearchResult {
    return listOf(
        Human("Luke", 75.0),
        Droid("C3PO", "translator"),
        Starship("FalconMillenium")
    ).random()
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
