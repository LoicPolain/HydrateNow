package be.shylo.hydratenow.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import be.shylo.hydratenow.R
import be.shylo.hydratenow.model.User
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

    private var user: User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        usernameEditText = findViewById(R.id.usernameEditText)
        emailEditText = findViewById(R.id.emailEditTextEmailAddress)
        passwordEditText = findViewById(R.id.passwordEditTextPassword)
        passwordConfirmEditText = findViewById(R.id.passwordConfirmEditTextPassword)
        registerBtn = findViewById(R.id.registerBtn)
        prograssBar = findViewById(R.id.progressBar)

        auth = Firebase.auth

        registerBtn.setOnClickListener {
            registerBtnClicked()
        }
    }

    private fun registerBtnClicked(){
        verifyUsername()
        verifyEmail()
        verifyPswd()

        if (user.isValid()){
            prograssBar.visibility = View.VISIBLE;
            registerBtn.isEnabled = false
            auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener(this){
                task ->
                if (task.isSuccessful) saveAdditionalUserData()
                else {
                    prograssBar.visibility = View.GONE
                    registerBtn.isEnabled = true
                    Toast.makeText(applicationContext,"Registration failed!", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun saveAdditionalUserData() {
        user.id = FirebaseAuth.getInstance().currentUser?.uid.toString()
        usersRef.child(user.id).setValue(user) { databaseError, databaseReference ->
            if (databaseError == null) {
                // Successful write to the database
                // You can consider it as the onComplete callback
                prograssBar.visibility = View.GONE
                registerBtn.isEnabled = true
                Toast.makeText(applicationContext,"Registration successful!", Toast.LENGTH_LONG).show()
            } else {
                // Handle the error
                prograssBar.visibility = View.GONE
                registerBtn.isEnabled = true
                Toast.makeText(applicationContext,"Registration failed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun verifyUsername(){
        val username: String = usernameEditText.text.toString().trim()
        if (username.isBlank()){
            usernameEditText.setError("Please, enter a valid username!")
            usernameEditText.requestFocus()
        }
        else user.username = username
    }

    private fun verifyEmail(){
        val email: String = emailEditText.text.toString().trim()
        if (email.isBlank()) {
            emailEditText.setError("Please, enter a valid e-mail!")
            emailEditText.requestFocus()
        }
        else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please, enter a valid e-mail!")
            emailEditText.requestFocus()
        }
        else user.email = email
    }

    private fun verifyPswd(){
        val pswd: String = passwordEditText.text.toString().trim()
        val pswdConfirm: String = passwordConfirmEditText.text.toString().trim();
        if (pswd.isBlank()) {
            passwordEditText.setError("Please, enter a valid password!")
            passwordEditText.requestFocus()
        }
        else if (pswd.length < 8) {
            passwordEditText.setError("The password need to be at least 8 characters long!")
            passwordEditText.requestFocus()
        }

        else if (pswdConfirm.isBlank()) {
            passwordConfirmEditText.setError("Please, enter a valid password!")
            passwordConfirmEditText.requestFocus()
        }
        else if (!pswdConfirm.equals(pswd)) {
            passwordConfirmEditText.setError("Both passwords need to match!")
            passwordConfirmEditText.requestFocus()
        }
        else user.password = pswd
    }

}