package be.shylo.hydratenow.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import be.shylo.hydratenow.R
import be.shylo.hydratenow.model.data.MyDatabase
import be.shylo.hydratenow.model.data.WaterLog
import be.shylo.hydratenow.model.data.repository.WaterLogRepo
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone


class WaterLogViewModel(application: Application): AndroidViewModel(application) {
    var dataChartIsSet: Boolean = false
    private val waterLogRepo: WaterLogRepo
    public var totalWaterAmount: Float = 0.0f

    init {
        val waterLogDao = MyDatabase.getDatabaseConnection(application).waterLogDao()
        waterLogRepo = WaterLogRepo(waterLogDao)
    }

    fun addWaterLog(waterLog: WaterLog){
        GlobalScope.launch {
            waterLogRepo.addWaterLog(waterLog)
        }
    }

    suspend fun getTotalWaterAmount(userId: String): Float {
        val tempDailyWaterlogs = ArrayList<ValueDataEntry>()
        val defaultTimeZone: TimeZone = TimeZone.getDefault()
        val calendar = Calendar.getInstance(defaultTimeZone)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val startTime = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 24)
        val endTime = calendar.timeInMillis

        val listWaterLogs = waterLogRepo.findWaterLogsBtwHours(startTime, endTime, userId)
        if (listWaterLogs != null) {
            listWaterLogs?.forEach {
                    waterLog ->  run {
                totalWaterAmount += waterLog.waterAmount
            }
            }
        }
        return totalWaterAmount
    }

    suspend fun getWaterLogs(userId: String): ArrayList<ValueDataEntry> {
        val tempDailyWaterlogs = ArrayList<ValueDataEntry>()
        val defaultTimeZone: TimeZone = TimeZone.getDefault()
        val calendar = Calendar.getInstance(defaultTimeZone)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        for (i in 0..23){
            calendar.set(Calendar.HOUR_OF_DAY, i)
            val startTime = calendar.timeInMillis
            calendar.set(Calendar.HOUR_OF_DAY, i+1)
            val endTime = calendar.timeInMillis

            val listWaterLogs = waterLogRepo.findWaterLogsBtwHours(startTime, endTime, userId)
            val timeTextLabel = (if (i<10) "0" else "") + i.toString() + ":00"
            if (listWaterLogs == null) tempDailyWaterlogs.add(ValueDataEntry(timeTextLabel, 0f))
            else{
                var sumWaterAmount: Float = 0f
                listWaterLogs.forEach{
                        waterLog -> run {
                    sumWaterAmount += waterLog.waterAmount
                }
                }
                tempDailyWaterlogs.add(ValueDataEntry(timeTextLabel, sumWaterAmount))
            }
        }
        return tempDailyWaterlogs

    }
}