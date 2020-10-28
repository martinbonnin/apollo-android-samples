import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.NormalizedCache
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo.coroutines.await
import com.example.GetCharactersQuery
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
      val cacheKeyResolver = object: CacheKeyResolver() {
        override fun fromFieldArguments(field: ResponseField, variables: Operation.Variables): CacheKey {
          return CacheKey.NO_KEY
        }

        override fun fromFieldRecordSet(field: ResponseField, recordSet: Map<String, Any>): CacheKey {
          val id = (recordSet.get("id") as? String)

          return id?.let { CacheKey.from(it) } ?: CacheKey.NO_KEY
        }

      }
      val apolloClient = ApolloClient.builder()
          .okHttpClient(OkHttpClient.Builder().addInterceptor(
              HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
              }
          ).build())
          .normalizedCache(SqlNormalizedCacheFactory("jdbc:sqlite:"), cacheKeyResolver)
          .serverUrl("http://localhost:8080/graphql")
          .build()

      apolloClient.query(GetCharactersQuery())
          .await()
          .let {
            println("got ${it.data} (fromCache: ${it.isFromCache})")
          }

      println(NormalizedCache.prettifyDump(apolloClient.apolloStore.normalizedCache().dump()))
      apolloClient.query(GetCharactersQuery())
          .await()
          .let {
            println("got ${it.data} (fromCache: ${it.isFromCache})")
          }

      println(NormalizedCache.prettifyDump(apolloClient.apolloStore.normalizedCache().dump()))
    }
    applicationContext.close()

  }
}