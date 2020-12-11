package include_on_fragment_classes

class DroidHero(
    val friendsConnection: FriendsConnection,
    val droidFragment: DroidFragment?) {
  class FriendsConnection(
      val name: String,
  )
}

class DroidFragment(
    val primaryFunction: String,
    val friendsConnection: FriendsConnection
) {
  class FriendsConnection(
      val name: String,
      val cursor: String,
      val friend: Friend?
  ) {
    class Friend(val name: String)
  }
}
