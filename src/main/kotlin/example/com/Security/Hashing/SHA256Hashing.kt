package example.com.Security.Hashing

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

class SHA256Hashing: HashService {
    override fun generateHash(value: String, saltLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val hexSalt = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex("$hexSalt$value")
        return SaltedHash(
            hash = hash,
            salt = hexSalt
        )
    }


    override fun verifyHash(value: String,saltedHash: SaltedHash): Boolean {
        return DigestUtils.sha256Hex(saltedHash.salt + value) == saltedHash.hash
    }
}