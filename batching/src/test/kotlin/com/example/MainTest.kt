package com.example

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.internal.batch.BatchConfig
import com.apollographql.apollo.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test

class MainTest {
  @Test
  fun test() {
    val apolloClient = ApolloClient.builder().serverUrl("https://apollo-fullstack-tutorial.herokuapp.com/graphql")
      .okHttpClient(
        OkHttpClient.Builder()
          .addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
          ).build()
      )
      .batchingConfiguration(BatchConfig(
        batchingEnabled = true,
      ))
      .build()

    apolloClient.startBatchPoller()
    runBlocking {
      launch {
        apolloClient.query(GetLaunchQuery("83")).toBuilder().canBeBatched(true).build().await()
      }
      apolloClient.query(GetLaunchesQuery()).toBuilder().canBeBatched(true).build().await()
      apolloClient.stopBatchPoller()
    }
  }
}