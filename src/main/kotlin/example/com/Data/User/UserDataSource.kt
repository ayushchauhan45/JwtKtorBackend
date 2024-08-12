package example.com.Data.User

interface UserDataSource {

    suspend fun getByUserUsername(username:String): User?

    suspend fun insertUser(user: User): Boolean
}