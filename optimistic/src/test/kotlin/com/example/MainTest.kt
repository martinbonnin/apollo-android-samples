package com.example

import GetStarrableQuery
import AddStarMutation
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.coroutines.toFlow
import fragment.StarrableFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test
import java.util.concurrent.TimeUnit

class MainTest {

  @Test
  fun test() {
    val mockServer = MockWebServer()
    mockServer.start()

    val cacheKeyResolver = object: CacheKeyResolver() {
      override fun fromFieldArguments(field: ResponseField, variables: Operation.Variables): CacheKey {
        return CacheKey.NO_KEY
      }

      override fun fromFieldRecordSet(field: ResponseField, recordSet: Map<String, Any>): CacheKey {
        return recordSet["id"]?.toString()?.let { CacheKey.from(it) } ?: CacheKey.NO_KEY
      }

    }
    val apolloClient = ApolloClient.builder()
      .serverUrl(mockServer.url("/"))
      .normalizedCache(LruNormalizedCacheFactory(evictionPolicy = EvictionPolicy.NO_EVICTION), cacheKeyResolver)
      .build()

    runBlocking {
      launch {
        mockServer.enqueue(
          MockResponse().setBody("""
            {
              "data": {
                "node": {
                  "__typename": "Repository",
                  "id": "42",
                  "stars": 1
                }
              }
            }
          """)
        )
        apolloClient.query(GetStarrableQuery()).watcher().toFlow().collect {
          // This will miss the optimistic data with stars = 2
          println("got ${it.data}")
        }
      }

      delay(1000)

      mockServer.enqueue(
        MockResponse().setBody("""
            {
              "data": {
                "addStar": {
                  "__typename": "Repository",
                  "id": "42",
                  "stars": 3
                }
              }
            }
          """).setBodyDelay(1, TimeUnit.SECONDS)
      )
      val mutationResult = apolloClient.mutate(
        AddStarMutation(),
        AddStarMutation.Data(
          AddStarMutation.AddStar(
            // __typename = "Repository",
            fragments = AddStarMutation.AddStar.Fragments(
              starrableFragment = StarrableFragment(id = "42", stars = 2)
            )
          )
        )
      ).await()

      apolloClient.apolloStore
    }
  }
}