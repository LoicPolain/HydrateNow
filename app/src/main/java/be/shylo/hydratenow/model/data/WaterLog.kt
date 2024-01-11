package be.shylo.hydratenow.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Calendar

@Entity(tableName = "water_log")
class WaterLog(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var waterAmount: Float,
    var dateTime: Long,
    var userId: String
) {
}