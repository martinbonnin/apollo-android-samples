import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.cache.http.ApolloHttpCache
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore
import com.apollographql.apollo.coroutines.await
import com.library.GetLaunchesQuery
import com.library.type.CustomType
import kotlinx.coroutines.runBlocking
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.io.File
import kotlin.test.Test

class MainTest {
  val response408 = MockResponse().setResponseCode(408).setBody("error")
  val response200 = MockResponse().setResponseCode(200).setBody("""
      {
        "data": {
          "launches": {
            "__typename": "LaunchConnection",
            "launches": [
              {
                "__typename": "Launch",
                "id": 1
              }
            ]
          }
        }
      }
    """.trimIndent())

  @Test
  fun test() {
    val cacheDir = File("apolloCache")
    cacheDir.deleteRecursively()
    val mockServer = MockWebServer()

    val apolloClient = ApolloClient
      .builder()
      .serverUrl(mockServer.url("/"))
      .httpCache(
        ApolloHttpCache(
          DiskLruHttpCacheStore(
            cacheDir,
            Long.MAX_VALUE
          )
        )
      )
      .defaultHttpCachePolicy(HttpCachePolicy.NETWORK_FIRST)
      .useHttpGetMethodForQueries(true)
      .useHttpGetMethodForPersistedQueries(true)
      .enableAutoPersistedQueries(true)
      .defaultHttpCachePolicy(HttpCachePolicy.NETWORK_FIRST)
      .okHttpClient(
        OkHttpClient
          .Builder()
          .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
          })
          .build()
      )
      .build()

    runBlocking {
      mockServer.enqueue(response200)
      apolloClient.query(GetLaunchesQuery()).await()
      mockServer.enqueue(response408)
      mockServer.enqueue(response408)
      apolloClient.query(GetLaunchesQuery()).await()
    }
  }


  @Test
  fun testOkHttp() {
    val cacheDir = File("okHttpCache")
    cacheDir.deleteRecursively()

    val mockServer = MockWebServer()
    val okHttpClient = OkHttpClient.Builder()
      .cache(Cache(cacheDir, Long.MAX_VALUE))
      .build()

    val request = Request.Builder()
      .get()
      .url(mockServer.url("/"))
      .build()


    mockServer.enqueue(response200)
    okHttpClient.newCall(request).execute()

    mockServer.enqueue(response408)
    mockServer.enqueue(response408)
    okHttpClient.newCall(request).execute()
  }
}