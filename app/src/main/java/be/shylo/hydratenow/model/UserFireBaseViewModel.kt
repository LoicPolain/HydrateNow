package be.shylo.hydratenow.model

import androidx.lifecycle.ViewModel

class UserFireBaseViewModel constructor(
    var id: String,
    var username: String,
    var email: String
): ViewModel() {

}