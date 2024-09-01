package example.com


import example.com.Data.User.UserDataService

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



    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureRouting()
}
