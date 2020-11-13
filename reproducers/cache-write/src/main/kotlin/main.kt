import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.library.GetConversationQuery
import com.library.fragment.MessagesFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer

fun message(index: Int) = GetConversationQuery.Message(
    fragments = GetConversationQuery.Message.Fragments(
        messagesFragment = MessagesFragment(
            iD = index.toString(),
            text = "this is message $index",
            direction = "direction $index",
            agent = MessagesFragment.Agent(fullName = "agent $index"),
            createdAt = "$index",
            hasUnsupportedContent = false,
            status = "ok"
        )
    ))

fun main(args: Array<String>) {
  val mockWebServer = MockWebServer().apply {
    start()
  }

  val cacheFactory = LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION)
  val apolloClient = ApolloClient.builder()
      .serverUrl(mockWebServer.url("/"))
      .normalizedCache(cacheFactory)
      .build()

  val query = GetConversationQuery("ID", Input.optional("null"), Input.optional(100))

  val data = GetConversationQuery.Data(
      conversation = GetConversationQuery.Conversation(
          listMessages = GetConversationQuery.ListMessages(
              messages = listOf(
                  message(0),
                  message(1)
              )
          )
      )
  )
  runBlocking {
    apolloClient.apolloStore.write(query, data).execute()
    val data2 = apolloClient.apolloStore.read(query).execute()
    println("data2=$data2")
    apolloClient.apolloStore.write(query, data.copy(
        conversation = data.conversation?.copy(
            listMessages = data.conversation?.listMessages.copy(
                messages = data.conversation?.listMessages?.messages + message(42)
            )
        )
    )).execute()
    val data3 = apolloClient.apolloStore.read(query).execute()
    println("data3=$data3")

  }

}