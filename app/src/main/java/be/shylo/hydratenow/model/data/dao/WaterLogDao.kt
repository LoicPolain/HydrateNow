package be.shylo.hydratenow.model.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import be.shylo.hydratenow.model.data.User
import be.shylo.hydratenow.model.data.WaterLog

@Dao
interface WaterLogDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addWaterLog(waterLog: WaterLog)
}