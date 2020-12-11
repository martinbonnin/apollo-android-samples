package include_on_fragment

class DroidHeroWithCondition(
    override val friendsConnection: DroidFragmentWithCondition.FriendsConnection,
    override val primaryFunction: String): DroidFragmentWithCondition {
  class FriendsConnection(
      val name: String,
      override val friend: Friend,
      override val cursor: String
  ): DroidFragmentWithCondition.FriendsConnection {
    class Friend(override val name: String): DroidFragmentWithCondition.FriendsConnection.Friend
  }
}

class DroidHero(
    override val primaryFunction: String,
    override val friendsConnection: FriendsConnection
    ): DroidFragment {
  class FriendsConnection(
      val name: String,
      override val cursor: String,
  ): DroidFragment.FriendsConnection
}

interface DroidFragment {
  val primaryFunction: String
  val friendsConnection: FriendsConnection
  interface FriendsConnection {
    val cursor: String
  }
}

interface DroidFragmentWithCondition: DroidFragment {
  interface FriendsConnection: DroidFragment.FriendsConnection {
    interface Friend {
      val name: String
    }
    val friend: Friend
  }
  override val friendsConnection: FriendsConnection
}