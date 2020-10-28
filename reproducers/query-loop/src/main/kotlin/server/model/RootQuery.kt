package server.model

import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class RootQuery: Query {

    fun user(): User {
        val primaryBuilding = Building(id = "primaryBuilding", name = Random.nextInt().toString())
        return User(
            id = "userId",
            primaryBuilding = primaryBuilding,
            buildings = listOf(primaryBuilding)
        )
    }
}

data class User(
    val id: String,
    val primaryBuilding: Building,
    val buildings: List<Building>
)

data class Building(
    val id: String,
    val name: String
)