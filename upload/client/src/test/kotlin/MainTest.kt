import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.FileUpload
import com.apollographql.apollo.coroutines.toDeferred
import com.example.UploadFileMutation
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.junit.Test
import server.runServer

class MainTest {

  @Test
  fun test() {
    val applicationContext = runServer()

    runBlocking {
      val apolloClient = ApolloClient.builder()
          .okHttpClient(OkHttpClient.Builder().addInterceptor(
              object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                  println("intercepting ${chain.request().url()}")
                  return chain.proceed(chain.request())
                }
              }
          ).build())
          .serverUrl("http://localhost:8080/graphql")
          .build()

      val fileUpload = FileUpload("application/octet-stream", "build.gradle.kts")
      apolloClient.mutate(UploadFileMutation(fileUpload))
          .toDeferred()
          .await()
          .let {
            println("got ${it.data} (fromCache: ${it.fromCache})")
          }

    }
    applicationContext.close()

  }
}