import client.GetDataQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.cache.CacheHeaders
import com.apollographql.apollo.cache.normalized.ApolloStore
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.apollographql.apollo.coroutines.toFlow
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.springframework.boot.runApplication
import server.ServerApplication

class MainTest {
  @Test
  fun `cacheSubscriberTest`() {
    val applicationContext = runApplication<ServerApplication>()

    runBlocking {
      val cache = LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION)

      val cacheKeyResolver = object: CacheKeyResolver() {
        override fun fromFieldArguments(field: ResponseField, variables: Operation.Variables): CacheKey {
          return CacheKey.NO_KEY
        }

        override fun fromFieldRecordSet(field: ResponseField, recordSet: Map<String, Any>): CacheKey {
          return CacheKey.NO_KEY
        }

      }
      val apolloClient = ApolloClient.builder()
          .serverUrl("http://localhost:8080/graphql")
          .normalizedCache(cache, cacheKeyResolver, true)
          .build()

      val subscriber = object : ApolloStore.RecordChangeSubscriber {
        override fun onCacheRecordsChanged(changedRecordKeys: Set<String>) {
          println("onCacheRecordsChanged $changedRecordKeys")
        }
      }
      apolloClient.apolloStore.subscribe(subscriber)
      async {
        apolloClient.query(GetDataQuery("user0"))
            .responseFetcher(ApolloResponseFetchers.CACHE_ONLY)
            .watcher()
            .toFlow().collect {
              println("watcher: " + it.data)
            }
      }

      apolloClient.query(GetDataQuery("user0"))
          .responseFetcher(ApolloResponseFetchers.CACHE_FIRST)
          .toFlow().collect {
            println(it.data)
          }
    }
    applicationContext.close()
  }
}