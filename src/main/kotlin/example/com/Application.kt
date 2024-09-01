package example.com


import example.com.Data.User.UserDataService
import example.com.Security.Hashing.SHA256Hashing
import example.com.Security.Token.JwtTokenService
import example.com.Security.Token.TokenConfig
import example.com.plugins.*
import io.ktor.server.application.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val dbname= "ktor-auth"
    val db = KMongo.createClient(
        connectionString = ""
    ).coroutine
        .getDatabase(dbname)
    val userDataSource = UserDataService(db)

    val tokenService = JwtTokenService()
    val tokenConfig =TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L *1000L* 60L*60L*24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashing = SHA256Hashing()


    configureSerialization()
    configureMonitoring()
    configureSecurity(tokenConfig)
    configureRouting(userDataSource , hashing, tokenService ,tokenConfig)
}
