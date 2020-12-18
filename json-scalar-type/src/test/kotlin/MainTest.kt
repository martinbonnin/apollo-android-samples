import client.AddProjectMutation
import client.GetProjectQuery
import client.JsonObject
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.CustomTypeValue
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.coroutines.toFlow
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import org.springframework.boot.runApplication
import server.ServerApplication


//object JsonObjectTypeAdapter : CustomTypeAdapter<JsonObject> {
//  override fun decode(value: CustomTypeValue<*>): JsonObject {
//    println("decode: '${value.value.toString()}'")
//    when {
//      value is CustomTypeValue.GraphQLJsonObject -> return LinkedTreeMap<String, Any>().apply {
//        putAll(value.value)
//      }
//      else -> throw IllegalStateException("")
//    }
//    return JsonObject(emptyMap<String, Any>())
//  }
//
//  override fun encode(value: JsonObject): CustomTypeValue<*> {
//    TODO("Not yet implemented")
//  }
//
//}

class MainTest {
  val apolloClient = ApolloClient.builder()
      //.addCustomTypeAdapter(CustomType.JSONOBJECT, JsonObjectTypeAdapter)
      .okHttpClient(OkHttpClient.Builder()
          .addInterceptor(HttpLoggingInterceptor().apply {
              setLevel(HttpLoggingInterceptor.Level.BODY)
          })
          .build()
      )
      .serverUrl("http://localhost:8080/graphql")
      .build()

  @Test
  fun `json object contains quotes`() {
    val applicationContext = runApplication<ServerApplication>()

    runBlocking {
      val apolloClient = ApolloClient.builder()
          //.addCustomTypeAdapter(CustomType.JSONOBJECT, JsonObjectTypeAdapter)
          .serverUrl("http://localhost:8080/graphql")
          .build()

      val channel = Channel<Unit>(capacity = Channel.UNLIMITED)
      val response = apolloClient.query(GetProjectQuery()).watcher().toFlow().collect {  }
      println(response.data?.project)
      delay(300000)
    }
    applicationContext.close()
  }

  @Test
  fun `json object can be used as input`() {
    val applicationContext = runApplication<ServerApplication>()

    runBlocking {
      val response = apolloClient.mutate(AddProjectMutation(jsonList = listOf(mapOf("key" to "value")))).await()
      println(response.data)
    }
    applicationContext.close()
  }
}