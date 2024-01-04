package be.shylo.hydratenow.model

class User constructor(
    var id: String,
    var username: String,
    var email: String,
    var password: String
) {
    // Empty (default) constructor
    constructor() : this("", "", "", "")

    fun isValid(): Boolean {
        return !(username.isBlank() && email.isBlank() && password.isBlank())
    }
}