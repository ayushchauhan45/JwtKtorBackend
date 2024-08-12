package example.com.plugins

import example.com.Data.User.UserDataSource
import example.com.Security.Hashing.HashService
import example.com.Security.Token.TokenConfig
import example.com.Security.Token.TokenService
import example.com.authenticate
import example.com.secretInfo
import example.com.singIn
import example.com.singup
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashService: HashService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        singup(userDataSource, hashService)
        singIn(userDataSource,hashService,tokenService,tokenConfig)
        authenticate()
        secretInfo()

    }
}
