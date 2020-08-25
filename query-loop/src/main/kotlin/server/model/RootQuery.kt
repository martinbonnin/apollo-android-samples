package server.model

import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class RootQuery: Query {

    fun tick(): Tick {
        return Tick(Random.nextInt())
    }
}


data class Tick(val count: Int)