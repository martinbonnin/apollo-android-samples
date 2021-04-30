import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.network.http.ApolloHttpNetworkTransport
import com.apollographql.apollo3.network.http.DefaultHttpEngine
import com.example.GetLaunchesQuery
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class MainTest {
  fun apolloClient(version: Int): ApolloClient {
    return ApolloClient(
      ApolloHttpNetworkTransport(
        VersionAwareRequestComposer(
          "https://apollo-fullstack-tutorial.herokuapp.com/graphql"
        ),
        DefaultHttpEngine()
      )
    ).withExecutionContext(CurrentVersion(version))
  }

  @Test
  fun testVersion2DoesntGetMissionPatch() = runBlocking {
    val response = apolloClient(2).query(GetLaunchesQuery())

    assert(response.data?.launches?.launches?.get(0)?.mission?.missionPatch is Optional.Absent)
  }

  @Test
  fun testVersion3GetsMissionPatch() = runBlocking {
    val response = apolloClient(3).query(GetLaunchesQuery())

    val missionPatch = response.data?.launches?.launches?.get(0)?.mission?.missionPatch?.getOrNull()
    assert(missionPatch != null)
  }
}
