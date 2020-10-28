package server.model

import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component

@Component
class RootQuery : Query {

  fun collections(): List<Collection> {
    return listOf(
      RegularCollection(
        listOf(
            RegularItem("item0"),
          ParticularItem("item1", "image1")
        )
      ),
      ParticularCollection(
        listOf(
          RegularItem("item2"),
          ParticularItem("item3", "image3")
        )
      )
    )
  }

  fun item(): List<Item> {
    return listOf(
        RegularItem("item4"),
        ParticularItem("item5", "image5")
    )
  }
}

interface Item {
  val title: String
}
class RegularItem(override val title: String) : Item
class ParticularItem(override val title: String, val image: String) : Item

interface Collection {
  val items: List<Item>
}
class RegularCollection(override val items: List<Item>) : Collection
class ParticularCollection(override val items: List<Item>) : Collection
