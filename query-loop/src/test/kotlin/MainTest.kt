import client.GetTickQuery
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
import kotlinx.coroutines.flow.delayEach
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Test
import org.springframework.boot.runApplication
import server.ServerApplication


class MainTest {
    /**
     * Only used to download the schema with
     * ./gradlew downloadApolloSchema --endpoint http://localhost:8080/graphql --schema src/main/graphql/schema.json
     */
    @Test
    fun `runServer`() {
        val applicationContext = runApplication<ServerApplication>()
        while (true) {
            Thread.sleep(1000)
        }
    }
    @Test
    fun `queryLoopTest`() {
        val applicationContext = runApplication<ServerApplication>()

        runBlocking {
            val cache = LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION)

            val apolloClient = ApolloClient.builder()
                .normalizedCache(cache)
                .serverUrl("http://localhost:8080/graphql")
                .build()

            launch {
                apolloClient.query(GetTickQuery())
                    .watcher()
                    .toFlow()
                    .onEach {
                        delay(500)
                    }
                    .collect {
                        println("watcher1 ${it.data} (fromCache: ${it.fromCache})")
                    }
            }

            launch {
                apolloClient.query(GetTickQuery())
                    .watcher()
                    .toFlow()
                    .onEach {
                        delay(500)
                    }
                    .collect {
                        println("watcher2 ${it.data} (fromCache: ${it.fromCache})")
                    }
            }
        }
        applicationContext.close()
    }
}