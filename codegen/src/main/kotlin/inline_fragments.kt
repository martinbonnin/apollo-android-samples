package inline_fragments

class Data(val animal: Animal) {
  interface Animal {
    val age: Int
  }

  interface WarmBlooded {
    val celsius: Float
    interface Pet {
      val name: String
    }
  }

  interface Pet {
    val nick: String
  }
}

interface BirdFragment {
  interface Eagle {
    val wingSize: Float
  }
  val feathers: Int
}