package example.com.Security.Hashing

data class SaltedHash(
    val hash: String,
    val salt: String,
)
