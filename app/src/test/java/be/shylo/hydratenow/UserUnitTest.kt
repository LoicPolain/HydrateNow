package be.shylo.hydratenow

import be.shylo.hydratenow.model.UserViewModel
import org.junit.Assert
import org.junit.Test

class UserUnitTest {
    @Test
    fun blankUserIsValid_isFalse() {
        val tempUser: UserViewModel = UserViewModel()
        Assert.assertFalse(tempUser.isValid())
    }
}