import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toFlow
import com.apollographql.apollo.subscription.SubscriptionConnectionParams
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport
import com.example.GetSecondsSubscription
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import org.springframework.boot.runApplication
import server.DefaultApplication

class MainTest {

  @Test
  fun test() {
    val applicationContext = runApplication<DefaultApplication>()

    runBlocking {
      var count = 0
      val okHttpClient = OkHttpClient.Builder()
          .addInterceptor(
              HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
              }
          )
          .build()

      val apolloClient = ApolloClient.builder()
          .okHttpClient(okHttpClient)
          .subscriptionTransportFactory(
              WebSocketSubscriptionTransport.Factory(
                  "http://localhost:8080/subscriptions",
                  okHttpClient
              )
          )
          .subscriptionConnectionParams {
            SubscriptionConnectionParams(mapOf("token" to (count++).toString()))
          }
          .serverUrl("http://localhost:8080/graphql")
          .build()

      launch {
        apolloClient.subscribe(GetSecondsSubscription())
            .toFlow()
            .collect {
              println("got $it")
            }
      }
      delay(15_000)
      println("reconnect")
      apolloClient.subscriptionManager.reconnect()
    }
    applicationContext.close()

  }
}