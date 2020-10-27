package server.model

import com.expediagroup.graphql.spring.operations.Subscription
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import server.DefaultGraphQLContext
import java.time.Duration

@Component
class RootSubscription : Subscription {
    val usedTokens = mutableSetOf<String>()

    fun secondsEllapsed(graphqlContext: DefaultGraphQLContext): Flux<String> {
        val token = graphqlContext.token
        graphqlContext.apply {
            println("secondsEllapsed: $this-${this.token}")
        }
        if (token == null) {
            throw IllegalStateException("token must not be null")
        }
        if (usedTokens.contains(token)) {
            throw IllegalStateException("token has already been used")
        }
        usedTokens.add(token)

        val start = System.currentTimeMillis()
        return Flux.interval(Duration.ofSeconds(0), Duration.ofSeconds(1)).map { it ->
            if (System.currentTimeMillis() - start > 10_000) {
                throw IllegalStateException("token has expired")
            }
            "$it seconds have ellapsed"
        }
    }
}
