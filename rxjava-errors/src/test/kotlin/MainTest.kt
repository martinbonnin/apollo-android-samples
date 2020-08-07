import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.CacheHeaders
import com.apollographql.apollo.cache.normalized.*
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Test

class MainTest {
  fun <T> from(operation: ApolloStoreOperation<T>): Single<T> {
    return Single.create { emitter ->
      println("Single.create")

      emitter.setCancellable {  }
      operation.enqueue(object : ApolloStoreOperation.Callback<T> {
        override fun onSuccess(result: T) {
          println("onSuccess ${emitter.isDisposed}")
          if (true || !emitter.isDisposed) {
            emitter.onError(Exception(""))
          }
        }

        override fun onFailure(t: Throwable) {
          println("onFailure")
          emitter.onError(t)
        }
      })
    }
  }


  @Test
  fun test() {
    val cacheFactory = object: NormalizedCacheFactory<NormalizedCache>() {
      override fun create(recordFieldAdapter: RecordFieldJsonAdapter): NormalizedCache {
        return object: NormalizedCache() {
          override fun clearAll() {
            TODO("Not yet implemented")
          }

          override fun loadRecord(key: String, cacheHeaders: CacheHeaders): Record? {
            TODO("Not yet implemented")
          }

          override fun performMerge(apolloRecord: Record, oldRecord: Record?, cacheHeaders: CacheHeaders): Set<String> {
            TODO("Not yet implemented")
          }

          override fun remove(cacheKey: CacheKey, cascade: Boolean): Boolean {
            TODO("Not yet implemented")
          }
        }
      }
    }
    val apolloClient = ApolloClient.builder()
        .normalizedCache(cacheFactory)
        .serverUrl("https://unused/")
        .build()

    val savedHandler = RxJavaPlugins.getErrorHandler()

    var undeliverableException: Throwable? = null
    RxJavaPlugins.setErrorHandler {
      undeliverableException = it
    }

    val operation = apolloClient.apolloStore.write(SomeQuery(), SomeQuery.Data(SomeQuery.Viewer("", "")))
    val testObserver = from(operation)
        .test()

    println(testObserver.errors())
    testObserver.dispose()

    Thread.sleep(1000)
    RxJavaPlugins.setErrorHandler(savedHandler)
  }
}