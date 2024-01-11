package be.shylo.hydratenow.model.data

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithWaterlogs(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val waterlogs: List<WaterLog>
) {

}