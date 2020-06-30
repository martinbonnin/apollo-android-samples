package server.model

import com.expediagroup.graphql.spring.operations.Subscription
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class MinutesEllapsedSubscription : Subscription {
    fun minutesEllapsed(): Flux<String> {
        return Flux.interval(Duration.ofSeconds(60)).flatMap { it ->
            Mono.justOrEmpty("$it minutes have elapsed")
        }
    }
}
