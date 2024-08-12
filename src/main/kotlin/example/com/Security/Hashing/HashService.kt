package example.com.Security.Hashing

interface HashService {
    fun generateHash(value:String, saltLength:Int = 32):SaltedHash
    fun verifyHash(value: String,saltedHash: SaltedHash):Boolean
}