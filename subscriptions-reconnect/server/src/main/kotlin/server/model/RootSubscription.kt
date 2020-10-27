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

    fun secondsElapsed(graphqlContext: DefaultGraphQLContext): Flux<String> {
        val token = graphqlContext.token
        graphqlContext.apply {
            println("secondsElapsed: $this-${this.token}")
        }
        if (token == null) {
            throw IllegalStateException("token must not be null")
        }
        if (usedTokens.contains(token)) {
            throw IllegalStateException("token has already been used")
        }
        usedTokens.add(token)

        val start = System.currentTimeMillis()

        return Flux.generate({0}, { state, sink ->
            if (state < 10) {
                sink.next("${state} seconds ellapsed")
                Thread.sleep(1000)
                state + 1
            } else {
                println("wait for token")
                Thread.sleep(1_000_000)
                -1
            }
        })
    }
}
