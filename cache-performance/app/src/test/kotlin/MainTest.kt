import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.NormalizedCacheFactory
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport
import okhttp3.OkHttpClient
import org.junit.Test

class MainTest {
    @Test
    fun `time to execute 1000 writes`() {

        val cache = SqlNormalizedCacheFactory
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