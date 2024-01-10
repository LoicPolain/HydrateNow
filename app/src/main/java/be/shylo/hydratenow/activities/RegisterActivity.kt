package be.shylo.hydratenow.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import be.shylo.hydratenow.R
import be.shylo.hydratenow.model.UserViewModel
import be.shylo.hydratenow.model.data.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var passwordConfirmEditText: EditText
    private  lateinit var registerBtn: Button
    private  lateinit var prograssBar: ProgressBar

    private lateinit var auth: FirebaseAuth;
    private val database = FirebaseDatabase.getInstance("https://hydratenow-15ac6-default-rtdb.europe-west1.firebasedatabase.app/")
    private  val usersRef = database.getReference("users")

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        usernameEditText = findViewById(R.id.usernameEditText)
        emailEditText = findViewById(R.id.emailEditTextEmailAddress)
        passwordEditText = findViewById(R.id.passwordEditTextPassword)
        passwordConfirmEditText = findViewById(R.id.passwordConfirmEditTextPassword)
        registerBtn = findViewById(R.id.registerBtn)
        prograssBar = findViewById(R.id.progressBar)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        auth = Firebase.auth

        registerBtn.setOnClickListener {
            registerBtnClicked()
        }
    }

    private fun registerBtnClicked(){
        verifyUsername()
        verifyEmail()
        verifyPswd()

        if (userViewModel.isValid()){
            prograssBar.visibility = View.VISIBLE;
            registerBtn.isEnabled = false
            auth.createUserWithEmailAndPassword(userViewModel.email, userViewModel.password).addOnCompleteListener(this){
                task ->
                if (task.isSuccessful) saveAdditionalUserData()
                else {
                    prograssBar.visibility = View.GONE
                    registerBtn.isEnabled = true
                    Toast.makeText(applicationContext,
                        getString(R.string.registration_failed), Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun saveAdditionalUserData() {
        userViewModel.id = auth.currentUser?.uid.toString()
        val user = User(userViewModel.id, userViewModel.username, userViewModel.email)
        userViewModel.addUser(user)

        usersRef.child(userViewModel.id).setValue(user) { databaseError, databaseReference ->
            if (databaseError == null) {
                prograssBar.visibility = View.GONE
                registerBtn.isEnabled = true
                Toast.makeText(applicationContext,
                    getString(R.string.registration_successful), Toast.LENGTH_LONG).show()

                //redirect the user to the login screen
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("currentUserEmail", userViewModel.email)
                intent.putExtra("currentUserPswd", userViewModel.password)
                startActivity(intent)
            } else {
                prograssBar.visibility = View.GONE
                registerBtn.isEnabled = true
                Toast.makeText(applicationContext,
                    getString(R.string.registration_failed), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun verifyUsername(){
        val username: String = usernameEditText.text.toString().trim()
        if (username.isBlank()){
            usernameEditText.setError(getString(R.string.please_enter_a_valid_username))
            usernameEditText.requestFocus()
        }
        else userViewModel.username = username
    }

    private fun verifyEmail(){
        val email: String = emailEditText.text.toString().trim()
        if (email.isBlank()) {
            emailEditText.setError(getString(R.string.please_enter_an_e_mail))
            emailEditText.requestFocus()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.please_enter_a_valid_e_mail))
            emailEditText.requestFocus()
        }
        else userViewModel.email = email
    }

    private fun verifyPswd(){
        val pswd: String = passwordEditText.text.toString().trim()
        val pswdConfirm: String = passwordConfirmEditText.text.toString().trim();
        if (pswd.isBlank()) {
            passwordEditText.setError(getString(R.string.please_enter_a_valid_password))
            passwordEditText.requestFocus()
        }
        else if (pswd.length < 8) {
            passwordEditText.setError(getString(R.string.the_password_need_to_be_at_least_8_characters_long))
            passwordEditText.requestFocus()
        }

        else if (pswdConfirm.isBlank()) {
            passwordConfirmEditText.setError(getString(R.string.please_enter_a_valid_password))
            passwordConfirmEditText.requestFocus()
        }
        else if (!pswdConfirm.equals(pswd)) {
            passwordConfirmEditText.setError(getString(R.string.both_passwords_need_to_match))
            passwordConfirmEditText.requestFocus()
        }
        else userViewModel.password = pswd
    }

}