package be.shylo.hydratenow.model.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import be.shylo.hydratenow.model.data.dao.UserDao
import be.shylo.hydratenow.model.data.dao.WaterLogDao

@Database(entities = arrayOf(User::class, WaterLog::class) , version = 1)
abstract class MyDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun waterLogDao(): WaterLogDao

    companion object{
        @Volatile
        private var connection: MyDatabase? = null

        fun getDatabaseConnection(context: Context): MyDatabase{
            val temp_connection = connection
            if (temp_connection != null) return temp_connection
            synchronized(this){
                val conn = Room.databaseBuilder(context.applicationContext, MyDatabase::class.java, name = "MyDatabase").build()
                connection = conn
                return conn
            }
        }
    }
}