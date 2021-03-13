import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.cache.http.ApolloHttpCache
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore
import com.apollographql.apollo.coroutines.await
import com.library.GetLaunchesQuery
import com.library.type.CustomType
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import kotlin.test.Test

class MainTest {

  @Test
  fun test() {
    val apolloClient =  ApolloClient
      .builder()
      .serverUrl("https://apollo-fullstack-tutorial.herokuapp.com/graphql")
      .httpCache(
        ApolloHttpCache(
          DiskLruHttpCacheStore(
            File("httpCache"),
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
//          .addInterceptor(providerHeaderInterceptor())
//          .connectTimeout(settings.timeout(), TimeUnit.SECONDS)
//          .readTimeout(settings.timeout(), TimeUnit.SECONDS)
//          .writeTimeout(settings.timeout(), TimeUnit.SECONDS)
          .build()
      )
      .build()

    runBlocking {
      val data = apolloClient.query(GetLaunchesQuery()).await()
      println(data)
    }
  }
}