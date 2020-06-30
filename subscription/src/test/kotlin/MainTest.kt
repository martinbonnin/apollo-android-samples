import client.GetProjectQuery
import client.JsonObject
import client.type.CustomType
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.CustomTypeValue
import com.apollographql.apollo.coroutines.toDeferred
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.springframework.boot.runApplication
import server.ServerApplication
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException


class MainTest {
    @Test
    fun `json object contains quotes`() {
        val applicationContext = runApplication<ServerApplication>()

        runBlocking {
            val apolloClient = ApolloClient.builder()
                //.addCustomTypeAdapter(CustomType.JSONOBJECT, JsonObjectTypeAdapter)
                .serverUrl("http://localhost:8080/graphql")
                .build()

            val response = apolloClient.query(GetProjectQuery()).toDeferred().await()
            println(response.data?.project)
            delay(300000)
        }
        applicationContext.close()
    }
}