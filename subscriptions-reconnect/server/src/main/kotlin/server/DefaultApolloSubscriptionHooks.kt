package server

import com.expediagroup.graphql.spring.execution.ApolloSubscriptionHooks
import kotlinx.coroutines.reactor.mono
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class DefaultApolloSubscriptionHooks : ApolloSubscriptionHooks {
  override fun onConnect(connectionParams: Map<String, String>, session: WebSocketSession, graphQLContext: Any?): Mono<Unit> = mono {
    (graphQLContext as DefaultGraphQLContext).token = connectionParams.get("token")
  }

}