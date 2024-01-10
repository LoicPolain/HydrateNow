package be.shylo.hydratenow.model.data.repository

import be.shylo.hydratenow.model.data.User
import be.shylo.hydratenow.model.data.dao.UserDao

class UserRepo(private val userDao: UserDao) {

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }
}