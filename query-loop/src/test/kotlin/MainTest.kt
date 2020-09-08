import client.FetchBuildingsQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.apollographql.apollo.coroutines.toFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.junit.Test
import org.springframework.boot.runApplication
import server.ServerApplication


class MainTest {
    /**
     * Only used to download the schema with
     * ./gradlew downloadApolloSchema --endpoint http://localhost:8080/graphql --schema src/main/graphql/client/schema.json
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

            val cacheKeyResolver = object: CacheKeyResolver() {
                override fun fromFieldArguments(field: ResponseField, variables: Operation.Variables): CacheKey {
                    return CacheKey.NO_KEY
                }

                override fun fromFieldRecordSet(field: ResponseField, recordSet: Map<String, Any>): CacheKey {
                    return (recordSet.get("id") as? String)?.let { CacheKey.from(it) } ?: CacheKey.NO_KEY
                }
            }

            val apolloClient = ApolloClient.builder()
                .normalizedCache(cache, cacheKeyResolver)
                .okHttpClient(OkHttpClient.Builder().addInterceptor(
                    object: Interceptor {
                        override fun intercept(chain: Interceptor.Chain): Response {
                            println("intercepting ${chain.request().url()}")
                            return chain.proceed(chain.request())
                        }
                    }
                ).build())
                .serverUrl("http://localhost:8080/graphql")
                .build()

            launch {
                apolloClient.query(FetchBuildingsQuery())
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
                apolloClient.query(FetchBuildingsQuery())
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