package be.shylo.hydratenow.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "water_log")
class WaterLog(
    @PrimaryKey(autoGenerate = true)
    var id: String,
    var waterAmount: Float,
    var dateTime: LocalDateTime,
    var user: User
) {
}