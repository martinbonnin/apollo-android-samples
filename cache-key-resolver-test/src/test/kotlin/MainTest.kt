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
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Test
import org.springframework.boot.runApplication
import server.ServerApplication


class MainTest {
    @Test
    fun `subscriptions runs for several minutes`() {
        val applicationContext = runApplication<ServerApplication>()

        val resolver: CacheKeyResolver = object : CacheKeyResolver() {
            override fun fromFieldRecordSet(field: ResponseField, recordSet: Map<String, Any>): CacheKey {
                return if (recordSet.containsKey("id") && recordSet["id"]!=null) {
                    val typeNameAndIDKey = recordSet["__typename"].toString().toLowerCase() + "." + recordSet["id"]
                    CacheKey.from(typeNameAndIDKey)
                } else {
                    CacheKey.NO_KEY
                }
            }

            override fun fromFieldArguments(field: ResponseField, variables: Operation.Variables): CacheKey {
                return CacheKey.NO_KEY
            }
        }

        runBlocking {
            val cache = LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION)//SqlNormalizedCacheFactory("jdbc:sqlite:")

            val apolloClient = ApolloClient.builder()
                .serverUrl("http://localhost:8080/graphql")
                .normalizedCache(cache, resolver)
                .subscriptionTransportFactory(WebSocketSubscriptionTransport.Factory("http://localhost:8080/subscriptions", OkHttpClient()))
                .build()

            apolloClient.query(GetDataQuery("user0"))
                .responseFetcher(ApolloResponseFetchers.CACHE_FIRST)
                .cacheHeaders(CacheHeaders
                    .builder()
                    //.addHeader(ApolloCacheHeaders.DO_NOT_STORE, "true")
                    .build())
                .toFlow().collect {
                println(it.data)
            }

            val dump = apolloClient.apolloStore.normalizedCache().dump()
            println("dump: " + NormalizedCache.prettifyDump(dump))

            apolloClient.query(GetDataQuery("user0")).responseFetcher(ApolloResponseFetchers.CACHE_FIRST).toFlow().collect {
                println(it.data)
                println("response.fromCache: ${it.fromCache}")
            }
        }
        applicationContext.close()
    }
}