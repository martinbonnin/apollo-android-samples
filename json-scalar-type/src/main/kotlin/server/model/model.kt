package server.model

import com.expediagroup.graphql.spring.operations.Mutation
import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component

@Component
class ProjectQuery : Query {
    fun project(): JSONObject = JSONObject(mapOf("name" to "json-scalar-type"))
}

class JSONObject(val map: Map<String, Any>)

@Component
class ProjectMutation : Mutation {
    fun addProjects(json: List<JSONObject>) = JSONObject(mapOf("name" to "json-scalar-type"))
}
