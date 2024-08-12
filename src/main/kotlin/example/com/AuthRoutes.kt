package example.com

import example.com.Data.AuthRequest
import example.com.Data.AuthResponse
import example.com.Data.User.User
import example.com.Data.User.UserDataService
import example.com.Data.User.UserDataSource
import example.com.Security.Hashing.HashService
import example.com.Security.Hashing.SaltedHash
import example.com.Security.Token.TokenClaim
import example.com.Security.Token.TokenConfig
import example.com.Security.Token.TokenService
import example.com.authenticate
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.singup(
    userDataSource: UserDataSource,
    hashService: HashService
){
    post("singup") {
        val request = call.receiveOrNull<AuthRequest>()?:kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val isFieldBlank = request.username.isBlank() || request.password.isBlank()
        val passwordLength = request.password.length < 8

        if (isFieldBlank||passwordLength){
            call.respond(HttpStatusCode.Conflict,"Check Password")
            return@post
        }

       val saltedHash = hashService.generateHash(
           request.password
       )

        val user = User(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )
        val wasAcknowledge = userDataSource.insertUser(user)
        if (!wasAcknowledge){
            call.respond(HttpStatusCode.Conflict,"Server was unable to respond")
            return@post
        }
         call.respond(HttpStatusCode.OK)
    }

}

fun Route.singIn(
    userDataSource: UserDataSource,
    hashService: HashService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
){
    post("singin"){
        val request = call.receiveOrNull<AuthRequest>()?:kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user= userDataSource.getByUserUsername(
            request.username
        )
        if (user == null){
            call.respond(HttpStatusCode.Conflict,"User was not found")
            return@post
        }

        val isValidPassword = hashService.verifyHash(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )

        )
        if (!isValidPassword){
            call.respond(HttpStatusCode.Conflict,"Wrong username or password")
            return@post
        }

       val token = tokenService.generateToken(
             config = tokenConfig,
           TokenClaim(
               name = "userId",
               value = user.id.toString()
           )
        )

       call.respond(status = HttpStatusCode.OK,
            message = AuthResponse(
                token = token
            ))


    }
}
fun Route.authenticate(){
    authenticate {
        get("authenticate"){
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.secretInfo(){
    authenticate {
        get("secretinfo") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK,"Your userId is $userId")
        }
    }
}