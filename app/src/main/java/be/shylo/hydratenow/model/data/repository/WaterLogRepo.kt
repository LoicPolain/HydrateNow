package be.shylo.hydratenow.model.data.repository

import be.shylo.hydratenow.model.data.WaterLog
import be.shylo.hydratenow.model.data.dao.WaterLogDao

class WaterLogRepo(private val waterLogDao: WaterLogDao) {
    suspend fun addWaterLog(waterLog: WaterLog){
        waterLogDao.addWaterLog(waterLog)
    }

    suspend fun findWaterLogsBtwHours(startTime: Long, endTime: Long, userId: String): List<WaterLog>?{
       return waterLogDao.findWaterLogsBtwHours(startTime, endTime, userId)
    }
}