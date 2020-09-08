package server.model

import com.expediagroup.graphql.spring.operations.Mutation
import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component
import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.random.Random

@Component
class RootQuery: Query {

    fun randomInt(): Int {
        return Random.nextInt()
    }
}

@Component
class RootMutation: Mutation {
    fun uploadFile(upload: Upload): Int {
        File("uploads").mkdirs()
        File("uploads/${upload.name}").writeBytes(Base64.getDecoder().decode(upload.base64))
        return Random.nextInt()
    }
}

class Upload(val name: String, val base64: String)
