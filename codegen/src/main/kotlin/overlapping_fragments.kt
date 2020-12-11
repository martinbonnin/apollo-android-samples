package overlapping_fragments


class Data(val hero: Hero) {
  interface Hero {
    val name: String
  }

  data class DroidHero(
      override val name: String,
      override val manual: Manual
  ) : Hero, DroidFragment, HeroFragment {
    data class Manual(
        override val language: String
    ): DroidFragment.Manual
  }

  data class HumanHero(
      override val name: String,
      override val bestFriend: BestFriend
  ) : Hero, HumanFragment, HeroFragment {
    interface BestFriend: HumanFragment.BestFriend {
      override val name: String
    }

    data class DroidBestFriend(
        override val manual: DroidFragment.Manual,
        override val name: String,
        override val primaryFunction: String
    ): HumanFragment.BestFriend, HumanFragment.BestFriend.Droid, DroidFragment, HeroFragment

    data class OtherBestFriend(
        override val name: String,
    ): HumanFragment.BestFriend, HeroFragment
  }

  data class OtherHero(
      override val name: String
  ): Hero
}

interface HeroFragment {
  val name: String
}
interface DroidFragment {
  interface Manual {
    val language: String
  }
  val manual: Manual
}

interface HumanFragment {
  val name: String
  val bestFriend: BestFriend

  interface BestFriend {
    val name: String
    interface Droid {
      val primaryFunction: String
    }
  }
}