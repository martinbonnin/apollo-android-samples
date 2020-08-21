import client.GetDataQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.cache.ApolloCacheHeaders
import com.apollographql.apollo.cache.CacheHeaders
import com.apollographql.apollo.cache.normalized.ApolloStore
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
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Test
import org.springframework.boot.runApplication
import server.ServerApplication


class MyFragment: Fragment() {
    val subscriber = object : ApolloStore.RecordChangeSubscriber {
        override fun onCacheRecordsChanged(changedRecordKeys: Set<String>) {
            println("onCacheRecordsChanged $changedRecordKeys")
        }

    }

    override fun onCreateView() {
        apolloClient.apolloStore.subscribe(subscriber)
    }
}

class MainTest {
    @Test
    fun `cacheKeyResolverTest`() {
        val applicationContext = runApplication<ServerApplication>()

        runBlocking {
            val cache = LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION)

            val apolloClient = ApolloClient.builder()
                .serverUrl("http://localhost:8080/graphql")
                .normalizedCache(cache)
                .build()

            val subscriber = object : ApolloStore.RecordChangeSubscriber {
                override fun onCacheRecordsChanged(changedRecordKeys: Set<String>) {
                    println("onCacheRecordsChanged $changedRecordKeys")
                }

            }
            apolloClient.apolloStore.subscribe(subscriber)
            apolloClient.query(GetDataQuery("user0"))
                .responseFetcher(ApolloResponseFetchers.CACHE_FIRST)
                .cacheHeaders(CacheHeaders
                    .builder()
                    .build())
                .toFlow().collect {
                println(it.data)
            }

            val dump = apolloClient.apolloStore.normalizedCache().dump()
            //println("dump: " + NormalizedCache.prettifyDump(dump))

            apolloClient.query(GetDataQuery("user0")).responseFetcher(ApolloResponseFetchers.CACHE_FIRST).toFlow().collect {
                //println(it.data)
                //println("response.fromCache: ${it.fromCache}")
            }
        }
        applicationContext.close()
    }
}