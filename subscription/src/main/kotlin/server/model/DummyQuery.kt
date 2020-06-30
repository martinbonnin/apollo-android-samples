package server.model

import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component

@Component
class DummyQuery: Query {
    fun dummy() = "dummy"
}