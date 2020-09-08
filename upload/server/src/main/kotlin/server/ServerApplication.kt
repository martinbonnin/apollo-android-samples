package server

import com.expediagroup.graphql.directives.KotlinDirectiveWiringFactory
import com.expediagroup.graphql.hooks.SchemaGeneratorHooks
import com.expediagroup.graphql.spring.execution.QueryHandler
import com.expediagroup.graphql.types.GraphQLRequest
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import graphql.schema.Coercing
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLType
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.Part
import org.springframework.web.reactive.function.server.*
import server.model.Upload
import org.springframework.boot.runApplication
import java.io.InputStream
import java.io.SequenceInputStream
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KType

@SpringBootApplication
class ServerApplication {
  @Bean
  fun wiringFactory() = KotlinDirectiveWiringFactory()

  @Bean
  fun hooks(wiringFactory: KotlinDirectiveWiringFactory) = CustomSchemaGeneratorHooks(wiringFactory)
}

fun runServer() = runApplication<ServerApplication>()

class CustomSchemaGeneratorHooks(override val wiringFactory: KotlinDirectiveWiringFactory) : SchemaGeneratorHooks {
  override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier as? KClass<*>) {
    Upload::class -> graphqlJSONType
    else -> null
  }
}

object JSONCoercing: Coercing<Upload, Map<*, *>> {
  override fun parseValue(input: Any?): Upload {
    check(input is Map<*, *>) {
      "Cannot parse $input"
    }
    return Upload(input.get("name") as String, input.get("base64") as String)
  }

  override fun parseLiteral(input: Any?): Upload {
    TODO("Not yet implemented")
  }

  override fun serialize(dataFetcherResult: Any): Map<*, *> {
    return throw CoercingSerializeException("")
  }
}

val graphqlJSONType = GraphQLScalarType.newScalar()
    .name("Upload")
    .description("A type representing a file upload")
    .coercing(JSONCoercing)
    .build()


@Configuration
class GraphQLMultipart(
    private val queryHandler: QueryHandler,
    private val objectMapper: ObjectMapper
) {
  @Bean
  @Order(1) // Should be before the [com.expediagroup.graphql.spring.RoutesConfiguration#graphQLRoutes]
  fun graphQLRoutesMultipart(): RouterFunction<ServerResponse> = coRouter {
    (POST("/graphql") and contentType(MediaType.MULTIPART_FORM_DATA)) { serverRequest ->
      val graphQLRequest = parsingMultipart(serverRequest)
      val graphQLResult = queryHandler.executeQuery(graphQLRequest)
      ok().json().bodyValueAndAwait(graphQLResult)
    }
  }

  @Suppress("BlockingMethodInNonBlockingContext")
  private suspend fun parsingMultipart(serverRequest: ServerRequest): GraphQLRequest {
    val parts: Map<String, Part> = serverRequest.bodyToFlux(Part::class.java)
        .doOnNext { part -> println("Part ${part.name()}") }
        .asFlow()
        .toList()
        .map { it.name() to it }
        .toMap()

    println("Part $parts")

    // Operations
    val operations: JsonNode = parts["operations"]
        ?.let { part ->
          objectMapper.readTree(part.inputStream())
        } ?: throw IllegalArgumentException("Missing 'operations' part")

    // Substitutions
    val part = parts["map"]
    if (part != null) {
      val typeReference = object: TypeReference<Map<String, List<String>>>() {}
      val substitutions = objectMapper.readValue(part.inputStream(), typeReference)
      println("Substitutions $substitutions")


      substitutions.forEach { (key, paths) ->
        println("key $key")
        val node = parts[key]?.let { it.toUpload(objectMapper) }
            ?: throw IllegalArgumentException("Part '$key' not found")

        paths.forEach { path ->
          println("apply $path $key")
          operations.substitute(path, node)
        }
      }
    }

    return objectMapper.treeToValue(operations, GraphQLRequest::class.java)
  }

  private suspend fun Part.inputStream(): InputStream =
      this.content()
          .map { it.asInputStream() }
          .reduce { a, b -> SequenceInputStream(a, b) }
          .awaitFirst()

  private fun JsonNode.substitute(paths: String, value: JsonNode): JsonNode =
      substituteAux(this, paths.split('.'), value)

  companion object {
    private tailrec fun substituteAux(node: JsonNode, paths: List<String>, value: JsonNode): JsonNode {
      if (paths.isEmpty()) return node

      val (path) = paths
      val tail = paths.drop(1)

      return if (paths.size == 1) {
        when (node) {
          is ObjectNode -> node.set(path, value)
          is ArrayNode -> node.set(path.toInt(), value) // FIXME could throw IndexOutOfBoundsException
          else -> throw IllegalStateException("Path '$path' not found for $node")
        }
      } else {
        val next = node.findNode(path)
        substituteAux(next, tail, value)
      }
    }

    private fun JsonNode.findNode(path: String): JsonNode =
        when (this) {
          is ObjectNode -> this.get(path) // FIXME might return null
          is ArrayNode -> this.get(path.toInt()) // FIXME might return null or fail with toInt, or
          else -> throw IllegalStateException("Path '$path' not found for $this")
        }

    private suspend fun Part.toUpload(mapper: ObjectMapper): JsonNode =
        content()
            .map { it.asInputStream() }
            .reduce { a, b -> SequenceInputStream(a, b) }
            .map { inputStream ->
              val bytes = inputStream.readBytes()
              val data = Base64.getEncoder().encodeToString(bytes)
              mapper.createObjectNode().apply {
                // simple parser for data like:
                // form-data; name="0"; filename="build.gradle.kts"
                val regex = Regex(".*filename *= *\"([^*]*)\"")
                val filename = headers().get("Content-Disposition")!!.first().let {
                  regex.matchEntire(it)!!.groupValues[1]
                }
                put("name", filename)
                put("base64", data)
              }
            }
            .awaitFirst()

  }

}

