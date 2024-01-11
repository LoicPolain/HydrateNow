package be.shylo.hydratenow.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import be.shylo.hydratenow.model.data.MyDatabase
import be.shylo.hydratenow.model.data.User
import be.shylo.hydratenow.model.data.repository.UserRepo
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {
    private val database = FirebaseDatabase.getInstance("https://hydratenow-15ac6-default-rtdb.europe-west1.firebasedatabase.app/").reference
    var userRepo: UserRepo
    var id: String = ""
    var username: String = ""
    var email: String = ""
    var password: String = ""

    init {
        val userDao = MyDatabase.getDatabaseConnection(application).userDao()
        userRepo = UserRepo(userDao)
    }

    fun isValid(): Boolean {
        return (!username.isBlank() && !email.isBlank() && !password.isBlank())
    }
    fun addUser(user: User){
        GlobalScope.launch {
            userRepo.addUser(user)
        }
    }

    fun checkUserInDB(user: User){
        GlobalScope.launch {
            val temp_user = userRepo.findUserById(user.id)
            if (temp_user == null) addUser(user)
        }
    }

    suspend fun findUserByid(userId: String): User? {
        val user = userRepo.findUserById(userId)
        return user
    }
}