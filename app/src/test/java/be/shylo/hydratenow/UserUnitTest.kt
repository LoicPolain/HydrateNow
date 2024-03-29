package be.shylo.hydratenow

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import be.shylo.hydratenow.model.UserViewModel
import be.shylo.hydratenow.model.data.MyDatabase
import be.shylo.hydratenow.model.data.User
import be.shylo.hydratenow.model.data.dao.UserDao
import be.shylo.hydratenow.model.data.repository.UserRepo
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class UserUnitTest {
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