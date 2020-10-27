package server


import com.expediagroup.graphql.execution.GraphQLContext
import com.expediagroup.graphql.spring.execution.GraphQLContextFactory
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component


class DefaultGraphQLContext(var token: String?) : GraphQLContext

@Component
class GraphQLHttpContextFactory: GraphQLContextFactory<DefaultGraphQLContext> {
  override suspend fun generateContext(
      request: ServerHttpRequest,
      response: ServerHttpResponse
  ): DefaultGraphQLContext {
    return DefaultGraphQLContext(
        token = request.headers.getFirst("token")
    ).apply {
      println("GraphQLHttpContextFactory: $this-${this.token}")
    }
  }
}