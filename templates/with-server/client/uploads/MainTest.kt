import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.FileUpload
import com.apollographql.apollo.coroutines.toDeferred
import com.example.UploadFileMutation
import com.example.UploadFilesMutation
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import server.runServer

class MainTest {

  @Test
  fun test() {
    val applicationContext = runServer()

    runBlocking {
      val apolloClient = ApolloClient.builder()
          .okHttpClient(OkHttpClient.Builder().addInterceptor(
              HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
              }
          ).build())
          .serverUrl("http://localhost:8080/graphql")
          .build()

      val fileUploads = listOf(
          FileUpload("application/octet-stream", "build.gradle.kts"),
          FileUpload("application/octet-stream", "src/test/kotlin/MainTest.kt"),
      )
      apolloClient.mutate(UploadFilesMutation(fileUploads))
          .toDeferred()
          .await()
          .let {
            println("got ${it.data} (fromCache: ${it.fromCache})")
          }

    }
    applicationContext.close()

  }
}