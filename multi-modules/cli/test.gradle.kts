tasks.register("hello") {
  doLast {
    configurations.named("apollo").get().incoming.files.let {
      println("it = $it")
    }
  }
}