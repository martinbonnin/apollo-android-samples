package server

import com.expediagroup.graphql.extensions.print
import com.expediagroup.graphql.hooks.SchemaGeneratorHooks
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.schema.GraphQLSchema
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

/**
 * A [SchemaGeneratorHooks] for [Apollo schema reporting](https://www.apollographql.com/docs/studio/schema/schema-reporting/)
 *
 * It will upload the schema at startup if APOLLO_SCHEMA_REPORTING is set to true. It use the following environment variables:
 * - APOLLO_KEY: mandatory, your API key, must begin with "service:"
 * - APOLLO_GRAPH_VARIANT: optional, defaults to "current"
 * - APOLLO_SERVER_PLATFORM: optional, defaults to "local"
 * - APOLLO_SERVER_USER_VERSION: optional
 * - APOLLO_SERVER_ID: optional
 *
 */
class ReportingSchemaGeneratorHooks : SchemaGeneratorHooks {
  private val logger = LoggerFactory.getLogger(ReportingSchemaGeneratorHooks::class.java)

  /**
   * @param variables a map representing the variable as Json values
   */
  private fun executeQuery(query: String, variables: Map<String, Any?>, key: String): String? {
    val mapper = ObjectMapper()
    val url = "https://schema-reporting.api.apollographql.com/api/graphql"

    val body = mapper.writeValueAsBytes(mapOf("query" to query, "variables" to variables)).toRequestBody("application/json".toMediaTypeOrNull())
    val request = Request.Builder()
        .post(body)
        .header("apollographql-client-name", "graphql-kotlin")
        .header("apollographql-client-version", SchemaGeneratorHooks::class.java.`package`.implementationVersion)
        .header("x-api-key", key)
        .url(url)
        .build()

    val response = OkHttpClient.Builder()
        .build()
        .newCall(request)
        .execute()

    return response.body?.string()
  }

  private fun String.sha256(): String {
    val bytes = toByteArray(charset = StandardCharsets.UTF_8)
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
  }

  private fun reportSchema(sdl: String, withExecutableSchema: Boolean, key: String): ReportResult {
    val query = """
        mutation ReportServerInfo(${'$'}info: EdgeServerInfo!, ${'$'}executableSchema: String) {
          me {
            __typename
            ... on ServiceMutation {
              reportServerInfo(info:${'$'}info, executableSchema: ${'$'}executableSchema) {
                __typename
                ... on ReportServerInfoError {
                  message
                  code
                }
                ... on ReportServerInfoResponse {
                  inSeconds
                  withExecutableSchema
                }
              }
            }
          }
        }
    """.trimIndent()

    val variables = mapOf(
        "info" to mapOf(
            "bootId" to UUID.randomUUID().toString(),
            "graphVariant" to (System.getenv("APOLLO_GRAPH_VARIANT") ?: "current"),
            // The infra environment in which this edge server is running, e.g. localhost, Kubernetes
            // Length must be <= 256 characters.
            "platform" to (System.getenv("APOLLO_SERVER_PLATFORM") ?: "local"),
            "runtimeVersion" to System.getProperty("java.version"),
            "executableSchemaId" to sdl.sha256(),
            // An identifier used to distinguish the version of the server code such as git or docker sha.
            // Length must be <= 256 charecters
            "userVersion" to System.getenv("APOLLO_SERVER_USER_VERSION"),
            // "An identifier for the server instance. Length must be <= 256 characters.
            "serverId" to (System.getenv("APOLLO_SERVER_ID")
                ?: System.getenv("HOSTNAME")
                ?: InetAddress.getLocalHost().getHostName()),
            "libraryVersion" to null,
        ),
        "executableSchema" to (if (withExecutableSchema) sdl else null)
    )

    return try {
      val responseBody = executeQuery(query, variables, key)


      val mapper = ObjectMapper()
      try {
        val node = mapper.readTree(responseBody!!)
        val reportServerInfo = node["data"]["me"]["reportServerInfo"]
        ReportResult.Success(reportServerInfo["inSeconds"].asLong(), reportServerInfo["withExecutableSchema"].asBoolean())
      } catch (e: Exception) {
        ReportResult.Error(Exception("Error while reporting info:\n$responseBody", e))
      }
    } catch (e: Exception) {
      ReportResult.Error(Exception("Cannot execute query", e))
    }
  }

  sealed class ReportResult {
    class Success(val inSeconds: Long, val withExecutableSchema: Boolean) : ReportResult()
    class Error(val cause: Throwable) : ReportResult()
  }

  private fun reportingLoop(sdl: String, key: String) {
    // TODO: maybe there's a more idiomatic way to do recurring tasks within spring boot?
    object : Thread() {
      init {
        isDaemon = true
        name = "Apollo schema reporting"
      }

      override fun run() {
        /**
         * Add some initial jitter
         */
        var inSeconds = Math.abs(Random().nextLong()) % 10
        var withExecutableSchema = false
        while (true) {
          sleep(inSeconds * 1000)
          logger.info("Apollo report schema (withExecutableSchema=$withExecutableSchema)...")
          when (val result = reportSchema(sdl, withExecutableSchema, key)) {
            is ReportResult.Success -> {
              logger.info("Apollo report schema success. Next in ${result.inSeconds} (withExecutableSchema=${result.withExecutableSchema})")
              inSeconds = result.inSeconds
              withExecutableSchema = result.withExecutableSchema
            }
            is ReportResult.Error -> {
              logger.error("Apollo report schema error. Stop reporting", result.cause)
              break
            }
          }
        }
      }
    }.start()
  }

  override fun willBuildSchema(builder: GraphQLSchema.Builder): GraphQLSchema.Builder {
    val key = System.getenv("APOLLO_KEY")
    if (key != null && System.getenv("APOLLO_SCHEMA_REPORTING") == "true") {
      reportingLoop(builder.build().print(), key)
    }
    return super.willBuildSchema(builder)
  }
}