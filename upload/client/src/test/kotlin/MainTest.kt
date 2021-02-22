import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.FileUpload
import com.apollographql.apollo3.coroutines.toDeferred
import com.example.UploadFileMutation
import com.example.UploadFilesMutation
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import server.runServer
import java.io.File

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
          FileUpload("application/octet-stream", File("build.gradle.kts")),
          FileUpload("application/octet-stream", File("src/test/kotlin/MainTest.kt")),
      )
      apolloClient.mutate(UploadFilesMutation(fileUploads))
          .toDeferred()
          .await()
          .let {
            println("got ${it.data} (fromCache: ${it.isFromCache})")
          }

    }
    applicationContext.close()
  }
}