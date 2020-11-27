import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import kotlinx.coroutines.runBlocking
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main(args: Array<String>) {
  val apolloClient = ApolloClient.builder()
      .serverUrl("https://concord.sandsmedia.com/graphql")
      .build()

  runBlocking {
    measureTime {
      val data = apolloClient.query(PeriodQuery()).await()
      println(data)
    }.let {
      println("Took: $it")
    }
  }
}