package com.paging

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingDataAdapter
import androidx.paging.PagingSource
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.NormalizedCache
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.apollographql.apollo.coroutines.toFlow
import com.squareup.moshi.Moshi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.first
import okio.buffer
import okio.source
import java.text.Normalizer
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

data class Launch(val id: String, val site: String)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cache = LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION)

        val apolloClient = ApolloClient.builder()
                .serverUrl("https://apollo-fullstack-tutorial.herokuapp.com/graphql")
                .normalizedCache(cache)
                .build()

        val diffCallback = object : DiffUtil.ItemCallback<Launch>() {
            override fun areItemsTheSame(oldItem: Launch, newItem: Launch): Boolean =
                    oldItem.id == newItem.id

            /**
             * Note that in kotlin, == checking on data classes compares all contents, but in Java,
             * typically you'll implement Object#equals, and use it to compare object contents.
             */
            override fun areContentsTheSame(oldItem: Launch, newItem: Launch): Boolean =
                    oldItem == newItem
        }
        val adapter = object : PagingDataAdapter<Launch, RecyclerView.ViewHolder>(diffCallback) {
            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                println("bind $position")
                (holder.itemView as TextView).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32f)
                (holder.itemView as TextView).setText(getItem(position)?.site)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                println("create")
                return object : RecyclerView.ViewHolder(TextView(parent.context)) {}
            }
        }

        val recyclerView = RecyclerView(this)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val pager = Pager(
                config = PagingConfig(
                        pageSize = 10,
                        enablePlaceholders = false
                ),
                pagingSourceFactory = { ApolloPagingSource(apolloClient) }
        ).flow

        lifecycleScope.launchWhenResumed {
            pager.collect {
                println("MARTIN WAS HERE AGAIN $it")
                adapter.submitData(it)
            }
        }

        recyclerView.setBackgroundColor(Color.RED)
        recyclerView.adapter = adapter
        setContentView(recyclerView)

        lifecycleScope.launchWhenResumed {
            delay(10000)
            val cache = apolloClient.apolloStore.normalizedCache().dump()
            println(NormalizedCache.prettifyDump(cache))

            // Triger an update
            val operation = GetLaunchesQuery(after = Input.absent(), pageSize = 30)
            val data = GetLaunchesQuery.Data(launches = GetLaunchesQuery.Launches(
                    cursor = "poison",
                    launches = listOf(
                            GetLaunchesQuery.Launch(id = "007",
                                    site = "Hello World"
                            )
                    )
            ))
            apolloClient.apolloStore.writeAndPublish(operation, data).execute()
        }
    }
}

class ApolloPagingSource(val apolloClient: ApolloClient) : PagingSource<String, Launch>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Launch> {

      val deferred = CompletableDeferred<GetLaunchesQuery.Data>()
        GlobalScope.launch {
          println("Start query ${params.key} ${params.loadSize}")
          apolloClient.query(GetLaunchesQuery(
                  after = Input.fromNullable(params.key),
                  pageSize = params.loadSize
          )).watcher().toFlow().collectIndexed { index, value ->
            println("Got Watcher $index")
            if (index == 0){
              deferred.complete(value.data!!)
            } else {
              println("Invalidate")
              invalidate()
              cancel()
            }
          }

        }

        val data = deferred.await()
        val list = data.launches.launches.map { Launch(site = it!!.site!!, id = it!!.id) }
        println("MARTIN WAS HERE ${list.size}")

        return LoadResult.Page(
                data = list,
                nextKey = data.launches.cursor.takeIf { it != "poison" },
                prevKey = null
        )
    }
}