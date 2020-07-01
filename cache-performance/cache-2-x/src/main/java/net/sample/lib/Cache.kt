package net.sample.lib

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ScalarTypeAdapters
import com.apollographql.apollo.api.internal.SimpleResponseWriter
import com.apollographql.apollo.cache.normalized.sql.ApolloSqlHelper
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.coroutines.toFlow
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.io.File


object Cache {
    val server = MockWebServer()

    fun apolloClient(context: Context, name: String): ApolloClient {
        val helper = ApolloSqlHelper(context, name)
        val cache = SqlNormalizedCacheFactory(helper)

        return ApolloClient.builder()
            .normalizedCache(cache)
            .dispatcher { it.run() }
            .serverUrl(server.url("/graphql"))
            .build()
    }

    val data = RepositoriesQuery.Data(
        viewer = RepositoriesQuery.Viewer(
            login = "toto",
            repositories = RepositoriesQuery.Repositories(
                nodes = getListOfRandomNodes()
            )
        )
    )

    private fun getListOfRandomNodes(): List<RepositoriesQuery.Node> {
        return 1.until(1000).map {
            RepositoriesQuery.Node(
                name = "This is a name ${Math.random()}",
                description = "${Math.random()} Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
            )
        }
    }

    fun plentyOfWrites(context: Context, iterations: Long) {

        val client = apolloClient(context, "cache-2-x")
        val query = RepositoriesQuery()
        //val writer = SimpleResponseWriter(ScalarTypeAdapters.DEFAULT)
        //data.marshaller().marshal(writer)
        //val jsonFile = File(context.filesDir, "data.json")
        //jsonFile.writeText(writer.toJson("  "))

        println("CachePerf: writing $iterations times in the cache using SQLDelight")
        1.until(iterations).forEach {
            client.apolloStore.write(query, data).execute()
        }
    }

    fun queriesWithCache(context: Context, iterations: Long) {
        val client = apolloClient(context, "cache-2-x")

        println("CachePerf doing $iterations queries")
        runBlocking {
            1.until(iterations).forEach {
                val jsonFile = File(context.filesDir, "data.json")
                val response = MockResponse().apply {
                    setBody(jsonFile.readText())
                    setHeader("Content-Type", "application/json")
                }
                server.enqueue(response)

                val query = RepositoriesQuery()
                client.query(query)
                    .responseFetcher(ApolloResponseFetchers.CACHE_AND_NETWORK)
                    .toFlow().collect {
                    println("got fromCache = ${it.fromCache}")
                }
            }
        }
    }
}