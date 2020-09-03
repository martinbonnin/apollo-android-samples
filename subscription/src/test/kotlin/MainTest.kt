import client.MinutesSubscription
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toFlow
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Test
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import server.ServerApplication


class MainTest {
  var applicationContext: ConfigurableApplicationContext? = null

  private fun startServer() {
    if (applicationContext == null) {
      applicationContext = runApplication<ServerApplication>()
    }
  }

  private fun stopServer() {
    applicationContext?.close()
    applicationContext = null
  }

  @Test
  fun `subscriptions runs for several minutes`() {
    startServer()
    runBlocking {
      val apolloClient = ApolloClient.builder()
          .serverUrl("http://localhost:8080/graphql")
          .subscriptionTransportFactory(WebSocketSubscriptionTransport.Factory("http://localhost:8080/subscriptions", OkHttpClient()))
          .build()

      apolloClient.addOnSubscriptionManagerStateChangeListener { from, to ->
        println("State changes from $from to $to")
      }
      startServer()
      var connected = true
      launch {
        delay(5000)
        connected = false
        stopServer()
        delay(5000)
        startServer()
        connected = true
      }
      apolloClient.subscribe(MinutesSubscription()).toFlow().retry {
        while (connected == false) {
          delay(200)
        }
        true
      }.collect {
        println(it.data?.minutesEllapsed)
      }
    }

  }
}