open class Config {
  val value = "value"

  open fun echoValue(): String {
    return value
  }
}

val configFactory: (Long) -> Config = {
  println("ojp")
  Config()
}