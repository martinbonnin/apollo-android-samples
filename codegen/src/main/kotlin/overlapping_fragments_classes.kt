package overlapping_fragments_classes

data class HumanFragment(
    val name: String,
    val bestFriend: BestFriend
) {
  data class BestFriend(
      val name: String,
      val onDroid: Droid?,
      val droidFragment: DroidFragment?
  ) {
    data class Droid(val primaryFunction: String)
  }
}

data class DroidFragment(
    val manual: Manual
) {
  data class Manual(val language: String)
}

data class HeroFragment(
    val name: String,
    val droidFragment: DroidFragment?,
    val humanFragment: HumanFragment?
)

data class Data(val hero: Hero) {
  data class Hero(
      val name: String,
      val heroFragment: HeroFragment
  )
}