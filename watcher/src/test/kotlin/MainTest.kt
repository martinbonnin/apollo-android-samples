import client.GetDataQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.cache.ApolloCacheHeaders
import com.apollographql.apollo.cache.CacheHeaders
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.NormalizedCache
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo.coroutines.toFlow
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Test
import org.springframework.boot.runApplication
import server.ServerApplication


class MainTest {
    @Test
    fun `cacheKeyResolverTest`() {
        val applicationContext = runApplication<ServerApplication>()

        runBlocking {
            val cache = SqlNormalizedCacheFactory("jdbc:sqlite:")
            cache.chain(LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION))

            val apolloClient = ApolloClient.builder()
                .serverUrl("http://localhost:8080/graphql")
                .normalizedCache(cache)
                .build()

            launch {
                apolloClient.query(GetDataQuery("user0"))
                    .toBuilder().apply {
                        responseFetcher(ApolloResponseFetchers.CACHE_ONLY)
                    }
                    .build()
                    .watcher()
                    .toFlow().collect {
                        println("watcher " + it.data)
                    }
            }

            apolloClient.query(GetDataQuery("user0"))
                .responseFetcher(ApolloResponseFetchers.CACHE_FIRST)
                .toFlow().collect {
                println(it.data)
                println("response.fromCache: ${it.fromCache}")
            }
        }
        applicationContext.close()
    }
}