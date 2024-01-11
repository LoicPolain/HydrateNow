package be.shylo.hydratenow.model.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import be.shylo.hydratenow.model.data.WaterLog

@Dao
interface WaterLogDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addWaterLog(waterLog: WaterLog)

    @Query("SELECT * FROM water_log WHERE dateTime >= :startTime AND dateTime < :endTime AND userId = :userId")
    suspend fun findWaterLogsBtwHours(startTime: Long, endTime: Long, userId: String): List<WaterLog>?
}