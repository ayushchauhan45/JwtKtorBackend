package example.com.Data

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val  token:String
)