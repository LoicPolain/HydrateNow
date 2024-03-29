package be.shylo.hydratenow.model.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import be.shylo.hydratenow.model.data.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun findUserById(userId: String): User?
}