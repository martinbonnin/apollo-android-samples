import client.LaunchesQuery
import client.LoginMutation
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.interceptor.ApolloAutoPersistedOperationInterceptor
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test

class MainTest {
    @Test
    fun `APQs work as expected`() {
        runBlocking {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
            val apolloClient = ApolloClient.builder()
                .serverUrl("https://apollo-fullstack-tutorial.herokuapp.com/graphql")
                .okHttpClient(okHttpClient)
                .setAutoPersistedOperationsInterceptorFactory(ApolloAutoPersistedOperationInterceptor.Factory())
                .build()


            if (false) {
                val response1 = apolloClient.query(LaunchesQuery()).toDeferred().await()
                println(response1.toString())
                val response2 = apolloClient.query(LaunchesQuery()).toDeferred().await()
                println(response2.toString())
            } else {
                val response1 = apolloClient.mutate(LoginMutation()).toDeferred().await()
                println(response1.toString())
                val response2 = apolloClient.mutate(LoginMutation()).toDeferred().await()
                println(response2.toString())
            }
        }
    }
}