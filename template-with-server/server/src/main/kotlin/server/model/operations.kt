package server.model

import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component

@Component
class RootQuery : Query {

  fun collections(): List<Collection> {
    return listOf(
      Collection(
        listOf(
          Item("item0"),
          ParticularItem("item1", "image1")
        )
      ),
      ParticularCollection(
        listOf(
          Item("item2"),
          ParticularItem("item3", "image3")
        )
      )
    )
  }
}

open class Item(val title: String)
class ParticularItem(title: String, val image: String) : Item(title)

open class Collection(open val items: List<Item>)
class ParticularCollection(override val items: List<Item>) : Collection(items)
