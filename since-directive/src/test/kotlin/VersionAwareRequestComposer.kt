import com.apollographql.apollo3.api.*
import com.apollographql.apollo3.api.http.*
import com.apollographql.apollo3.graphql.ast.*

class VersionAwareRequestComposer(val endpoint: String) : HttpRequestComposer {

  override fun <D : Operation.Data> compose(apolloRequest: ApolloRequest<D>): HttpRequest {
    val operation = apolloRequest.operation
    val currentVersion = apolloRequest.executionContext[CurrentVersion]?.version ?: error("Missing CurrentVersion in the ApolloClient executionContext")

    val body = DefaultHttpRequestComposer.buildPostBody(
      operation,
      apolloRequest.executionContext[ResponseAdapterCache]!!,
      false,
      stripUnsupportedFields(operation.document(), currentVersion)
    )

    return HttpRequest(
      method = HttpMethod.Post,
      url = endpoint,
      headers = emptyMap(),
      body = body
    )
  }

  private fun stripUnsupportedFields(originalDocumentString: String, currentVersion: Int): String {

    val document = GraphQLParser.parseDocument(originalDocumentString).orThrow()

    val transformed = document.transform {
      if (it is GQLField && (it.minVersion() ?: 0) > currentVersion) {
        null
      } else {
        it
      }
    }?.transform {
      if (it is GQLDirective && (it.name == "since" || it.name == "optional")) {
        null
      } else {
        it
      }
    }

    return transformed!!.toUtf8WithIndents()
  }

  private fun GQLField.minVersion(): Int? {
    val directive = directives.firstOrNull { it.name == "since" }

    if (directive == null) {
      return null
    }

    val argument = directive.arguments?.arguments?.first()

    check(argument != null && argument.name == "version") {
      "@since requires a single 'version' argument"
    }

    val value = argument.value
    check(value is GQLIntValue) {
      "@since requires an Int argument (is ${argument.value})"
    }

    return value.value
  }
}

class CurrentVersion(val version: Int): ClientContext(Key) {
  companion object Key: ExecutionContext.Key<CurrentVersion>
}