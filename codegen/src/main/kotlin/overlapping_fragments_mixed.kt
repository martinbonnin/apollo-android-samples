package overlapping_fragments_mixed

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
  interface Hero {
    val name: String
  }

  data class DroidHero(
      override val name: String,
      val manual: Manual,
  ) : Hero {
    val droidFragment = DroidFragment(
        manual = DroidFragment.Manual(
            language = manual.language
        )
    )
    data class Manual(
        override val language: String
    ) : overlapping_fragments.DroidFragment.Manual
  }

  data class HumanHero(
      override val name: String,
      val bestFriend: BestFriend
  ) : Hero {
    interface BestFriend {
      val name: String
    }

    data class DroidBestFriend(
        val manual: overlapping_fragments.DroidFragment.Manual,
        override val name: String,
        val primaryFunction: String
    ) : BestFriend

    data class OtherBestFriend(
        override val name: String,
    ) : BestFriend
  }

  data class OtherHero(
      override val name: String
  ) : Hero
}
