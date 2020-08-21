package server.model

import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component

@Component
class RootQuery: Query {
    fun unfinishedCompositions(input: CompositionInput): Composition {
        return Composition(0)
    }
}


data class Composition(val count: Int) {
    fun results(): List<Result> {
        return listOf(
            Result("42"),
            Result("43")
        )
    }
}

data class Result(val id: String) {
    fun metadata(): Metadata {
        return Metadata("someMetadata")
    }
}

data class Metadata(val status: String)
data class CompositionInput(val userId: String, val includeMetadata: Boolean)