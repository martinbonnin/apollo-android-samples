import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.NormalizedCache
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.apollographql.apollo.coroutines.await
import com.example.GetContentQuery
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import server.runServer

class MainTest {

  @Test
  fun test() {
    val applicationContext = runServer()

    runBlocking {
      val apolloClient = ApolloClient.builder()
          .okHttpClient(OkHttpClient.Builder().addInterceptor(
              HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
              }
          ).build())
          .normalizedCache(LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION))
          .serverUrl("http://localhost:8080/graphql")
          .build()

      apolloClient.query(GetContentQuery(listOf(0), listOf(1), listOf(2)))
          .await()
          .let {
            println("got ${it.data} (fromCache: ${it.fromCache})")
          }

      println(NormalizedCache.prettifyDump(apolloClient.apolloStore.normalizedCache().dump()))
      apolloClient.query(GetContentQuery(listOf(0), listOf(1), listOf(3)))
          .await()
          .let {
            println("got ${it.data} (fromCache: ${it.fromCache})")
          }

      println(NormalizedCache.prettifyDump(apolloClient.apolloStore.normalizedCache().dump()))
    }
    applicationContext.close()

  }
}