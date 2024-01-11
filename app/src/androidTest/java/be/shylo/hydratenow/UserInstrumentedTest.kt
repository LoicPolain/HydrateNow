package be.shylo.hydratenow

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import be.shylo.hydratenow.model.UserViewModel
import be.shylo.hydratenow.model.data.MyDatabase
import be.shylo.hydratenow.model.data.User
import be.shylo.hydratenow.model.data.dao.UserDao
import be.shylo.hydratenow.model.data.repository.UserRepo
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UserInstrumentedTest {
    private lateinit var userDao: UserDao
    private lateinit var db: MyDatabase
    private lateinit var user: User


    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), MyDatabase::class.java)
            .allowMainThreadQueries() // Allow queries on the main thread for testing purposes
            .build()

        userDao = db.userDao()
        user = User("1", "testUser", "testuser@gmail.com")
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun addUserAndRetrieve() = runBlocking{
        userDao.addUser(user)
        val retrievedUser = userDao.findUserById(user.id)
        assertEquals(user, retrievedUser)
    }
}