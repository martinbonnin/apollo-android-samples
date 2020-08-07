import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.CacheHeaders
import com.apollographql.apollo.request.RequestHeaders
import com.library.AddBookMutation
import kotlin.test.Test

class MainTest {

  @Test
  fun test() {
    val apolloClient = ApolloClient.builder().serverUrl("https://localhost:8080").build()

    val call = apolloClient.mutate(AddBookMutation("1984")).toBuilder()
        .requestHeaders(RequestHeaders.builder().addHeader("toto", "tata").build())
        .cacheHeaders(CacheHeaders.builder().addHeader("titi", "tutu").build())
        .build()

    println("this is a test")
  }
}