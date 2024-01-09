package be.shylo.hydratenow.model

import androidx.lifecycle.ViewModel

class UserViewModel constructor(
    var id: String,
    var username: String,
    var email: String,
    var password: String
): ViewModel() {

    constructor() : this("", "", "", "")

    fun isValid(): Boolean {
        return !(username.isBlank() && email.isBlank() && password.isBlank())
    }
}