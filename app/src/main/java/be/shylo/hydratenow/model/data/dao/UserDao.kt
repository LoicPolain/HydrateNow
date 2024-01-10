package be.shylo.hydratenow.model.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import be.shylo.hydratenow.model.data.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addUser(user: User)
}