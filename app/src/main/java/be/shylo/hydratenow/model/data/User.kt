package be.shylo.hydratenow.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    var id: String,
    var username: String,
    var email: String
) {

}