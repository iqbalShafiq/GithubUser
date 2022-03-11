package space.iqbalsyafiq.githubuser.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import space.iqbalsyafiq.githubuser.model.UserResponse

@Dao
interface UserDao {
    // Insert user to favorite list
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserToFavorite(user: UserResponse): Long

    // Get users
    @Query("SELECT * FROM userresponse")
    suspend fun getUsers(): List<UserResponse>

    // Get user by username
    @Query("SELECT * FROM userresponse WHERE login = :username")
    suspend fun getUser(username: String): UserResponse?

    // Delete user by Id
    @Query("DELETE FROM userresponse WHERE login = :username")
    suspend fun deleteUser(username: String)
}