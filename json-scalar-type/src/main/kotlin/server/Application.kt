package server

import com.expediagroup.graphql.directives.KotlinDirectiveWiringFactory
import com.expediagroup.graphql.hooks.SchemaGeneratorHooks
import graphql.schema.Coercing
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import server.model.JSONObject
import kotlin.reflect.KClass
import kotlin.reflect.KType

@SpringBootApplication
class ServerApplication {
    @Bean
    fun wiringFactory() = CustomDirectiveWiringFactory()

    @Bean
    fun hooks(wiringFactory: KotlinDirectiveWiringFactory) = CustomSchemaGeneratorHooks(wiringFactory)

}

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}

class CustomDirectiveWiringFactory : KotlinDirectiveWiringFactory()


class CustomSchemaGeneratorHooks(override val wiringFactory: KotlinDirectiveWiringFactory) : SchemaGeneratorHooks {
    override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier as? KClass<*>) {
        JSONObject::class -> graphqlJSONType
        else -> null
    }
}

object JSONCoercing: Coercing<JSONObject, Map<*, *>> {
    override fun parseValue(input: Any?): JSONObject {
        TODO("Not yet implemented")
    }

    override fun parseLiteral(input: Any?): JSONObject {
        TODO("Not yet implemented")
    }

    override fun serialize(dataFetcherResult: Any): Map<*, *> {
        return when  {
            dataFetcherResult is JSONObject -> dataFetcherResult.map
            else -> throw CoercingSerializeException("")
        }
    }
}

val graphqlJSONType = GraphQLScalarType.newScalar()
    .name("JSONObject")
    .description("A type representing a JsonObject")
    .coercing(JSONCoercing)
    .build()
