package net.sample.lib

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.sql.ApolloSqlHelper
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import okhttp3.mockwebserver.MockWebServer

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

    fun generateData():RepositoriesQuery.Data {
        return RepositoriesQuery.Data(
            viewer = RepositoriesQuery.Viewer(
                login = "login",
                repositories = RepositoriesQuery.Repositories(
                    nodes = 1.until(1000).map {
                        RepositoriesQuery.Node(
                            name = "This is a name ${Math.random()}",
                            description = "${Math.random()} Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
                        )
                    }
                )
            )
        )
    }

    fun plentyOfWrites(context: Context, iterations: Long) {
        val helper = ApolloSqlHelper(context, "cache-1-4")
        val cache = SqlNormalizedCacheFactory(helper)

        val client = ApolloClient.builder()
            .normalizedCache(cache)
            .serverUrl("https://net.sample/graphql")
            .build()

        val query = RepositoriesQuery()

        println("CachePerf: writing $iterations times in the cache using SQLite directly")
        1.until(iterations).forEach {
            client.apolloStore.write(query, generateData()).execute()
        }
    }
}