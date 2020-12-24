import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import com.library.HomeQuery
import com.library.fragment.SectionFragment
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.util.concurrent.Executors

fun main(args: Array<String>) {
    val mockServer = MockWebServer()
    val okHttpClient = OkHttpClient()
    val dispatcher = Executors.newCachedThreadPool()

    val cacheKeyResolver = object : CacheKeyResolver() {
        override fun fromFieldArguments(field: ResponseField, variables: Operation.Variables): CacheKey {
            return CacheKey.NO_KEY
        }

        override fun fromFieldRecordSet(field: ResponseField, recordSet: Map<String, Any>): CacheKey {
            return (recordSet["id"] as? String)?.let { CacheKey.from(it) } ?: CacheKey.NO_KEY
        }
    }
    val apolloClient = ApolloClient.builder()
            .normalizedCache(LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION), cacheKeyResolver)
            .okHttpClient(okHttpClient)
            .dispatcher(dispatcher)
            .serverUrl(mockServer.url("/"))
            .build()

    mockServer.enqueue(MockResponse().setBody("""
        {
          "data": {
            "home": {
              "__typename": "Home",
              "sectionA": {
                "__typename": "SectionA",
                "name": "initialSectionName",
                "id": "section-id",
                "imageUrl": "initialUrl"
              }
            }
          }
        }
    """.trimIndent()))


    runBlocking {
        val networkResponse = apolloClient.query(HomeQuery()).await()

        check(networkResponse.data?.home?.sectionA?.name == "initialSectionName")
        apolloClient.apolloStore.writeAndPublish(
                HomeQuery(),
                HomeQuery.Data(
                        HomeQuery.Home(
                                sectionA = HomeQuery.SectionA(
                                        name = "modifiedSectionName"
                                ),
                                fragments = HomeQuery.Home.Fragments(
                                        sectionFragment = SectionFragment(
                                                sectionA = SectionFragment.SectionA(
                                                        id = "section-id",
                                                        imageUrl = "modifiedUrl",
                                                ),
                                        )
                                )
                        )
                )
        ).execute()

        val cacheResponse = apolloClient.query(HomeQuery())
                .toBuilder()
                .responseFetcher(ApolloResponseFetchers.CACHE_ONLY)
                .build()
                .await()

        check(cacheResponse.data?.home?.sectionA?.name == "modifiedSectionName")
    }

    dispatcher.shutdown()
    mockServer.shutdown()
    okHttpClient.dispatcher.executorService.shutdown()
    okHttpClient.connectionPool.evictAll()
}