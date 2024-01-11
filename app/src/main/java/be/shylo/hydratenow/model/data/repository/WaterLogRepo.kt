package be.shylo.hydratenow.model.data.repository

import be.shylo.hydratenow.model.data.User
import be.shylo.hydratenow.model.data.WaterLog
import be.shylo.hydratenow.model.data.dao.WaterLogDao

class WaterLogRepo(private val waterLogDao: WaterLogDao) {
    suspend fun addWaterLog(waterLog: WaterLog){
        waterLogDao.addWaterLog(waterLog)
    }
}