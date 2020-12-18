package polymorphic_fragment

class Data {
  class HumanHero(override val name: String,
                  override val bestFriend: BestFriend
  ): HumanHeroFragment {
    interface BestFriend: HumanFragment.BestFriend {
      override val name: String
    }
    class CharacterBestFriend(override val name: String) : BestFriend
    class DroidBestFriend(override val name: String, override val primaryFunction: String, override val manual: DroidFragment.Manual) : HumanFragment.DroidBestFriend
  }

  class DroidHero(override val name:String,
                  override val manual: DroidFragment.Manual): DroidHeroFragment
}
interface HeroFragment {
  val name: String
}

interface HumanHeroFragment: HeroFragment, HumanFragment

interface DroidHeroFragment: HeroFragment, DroidFragment

interface DroidFragment {
  interface Manual {
    val language: String
  }
  val manual: Manual
}

interface HumanFragment {
  interface BestFriend {
    val name: String
  }

  interface DroidBestFriend: BestFriend, DroidFragment {
    val primaryFunction: String
  }

  val name: String
  val bestFriend: BestFriend
}
