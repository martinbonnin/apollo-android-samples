package server.model

import com.expediagroup.graphql.spring.operations.Mutation
import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component
import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.random.Random

@Component
class RootQuery: Query {

    fun content(where: Where): Content {
        return Content("id")
    }
}

class Content(val id: String)

class State(val _eq: String, val numberInput: Int)
class VendorId(val _in: List<Int>)
class TagId(val _in: List<Int>)
class RecipeTag(val tag_id: TagId)
class Filter(val recipe_tags: RecipeTag)
class Where (val state: State, val vendor_id: VendorId, val _and: List<Filter>)
