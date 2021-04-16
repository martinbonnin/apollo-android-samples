package server.model

import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component

@Component
class RootQuery : Query {

  fun conferences(): List<Conference> {
    return conferences
  }
}

class Conference(
  val startTime: String,
  val endTime: String,
  val title: String,
  val online: Boolean,
  val url: String,
  val location: Location,
  val cfp: Cfp?
)

interface Location

class Online(val url: String): Location
class Physical(
  val city: String,
  val country: String
): Location

class Cfp(
  val url: String,
  val startTime: String,
  val endTime: String
)

