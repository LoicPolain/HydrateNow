package be.shylo.hydratenow.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import be.shylo.hydratenow.model.data.MyDatabase
import be.shylo.hydratenow.model.data.WaterLog
import be.shylo.hydratenow.model.data.repository.WaterLogRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WaterLogViewModel(application: Application): AndroidViewModel(application) {
    private val waterLogRepo: WaterLogRepo

    init {
        val waterLogDao = MyDatabase.getDatabaseConnection(application).waterLogDao()
        waterLogRepo = WaterLogRepo(waterLogDao)
    }

    fun addWaterLog(waterLog: WaterLog){
        GlobalScope.launch {
            waterLogRepo.addWaterLog(waterLog)
        }
    }
}