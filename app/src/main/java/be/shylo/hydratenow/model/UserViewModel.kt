package be.shylo.hydratenow.model

import android.app.Application
import android.provider.Settings.System.getString
import androidx.lifecycle.AndroidViewModel
import be.shylo.hydratenow.R
import be.shylo.hydratenow.model.data.MyDatabase
import be.shylo.hydratenow.model.data.User
import be.shylo.hydratenow.model.data.repository.UserRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {
    private val userRepo: UserRepo
    var id: String = ""
    var username: String = ""
    var email: String = ""
    var password: String = ""

    init {
        val userDao = MyDatabase.getDatabaseConnection(application).userDao()
        userRepo = UserRepo(userDao)
    }

    fun isValid(): Boolean {
        return !(username.isBlank() && email.isBlank() && password.isBlank())
    }
    fun addUser(user: User){
        GlobalScope.launch {
            userRepo.addUser(user)
        }
    }
}