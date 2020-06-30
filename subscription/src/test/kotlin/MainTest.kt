import client.MinutesSubscription
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toFlow
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Test
import org.springframework.boot.runApplication
import server.ServerApplication


class MainTest {
    @Test
    fun `subscriptions runs for several minutes`() {
        val applicationContext = runApplication<ServerApplication>()

        runBlocking {
            val apolloClient = ApolloClient.builder()
                .serverUrl("http://localhost:8080/graphql")
                .subscriptionTransportFactory(WebSocketSubscriptionTransport.Factory("http://localhost:8080/subscriptions", OkHttpClient()))
                .build()

            apolloClient.subscribe(MinutesSubscription()).toFlow().collect {
                println(it.data?.minutesEllapsed)
            }
        }
        applicationContext.close()
    }
}