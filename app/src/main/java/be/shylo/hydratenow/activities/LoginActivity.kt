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
import be.shylo.hydratenow.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private  lateinit var loginBtn: Button
    private  lateinit var forgotPswdBtn: Button
    private  lateinit var prograssBar: ProgressBar

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailLoginEditTextEmailAddress)
        passwordEditText = findViewById(R.id.passwordLoginEditTextPassword)
        loginBtn = findViewById(R.id.loginBtn)
        forgotPswdBtn = findViewById(R.id.forgotPswdBtn)
        prograssBar = findViewById(R.id.loginProgressBar)

        val currentUserEmail = intent.getStringExtra("currentUserEmail")

        if (!currentUserEmail.isNullOrEmpty()){
            emailEditText.setText(currentUserEmail)
        }

        auth = Firebase.auth

        loginBtn.setOnClickListener {
            loginBtnClicked()
        }

        forgotPswdBtn.setOnClickListener {
            forgotPswdBtnClicked()
        }

    }

    private fun forgotPswdBtnClicked() {
        val intent = Intent(this@LoginActivity, ForgotPswdActivity::class.java)
        intent.putExtra("currentUserEmail", emailEditText.text.toString().trim())
        startActivity(intent)
    }

    private fun loginBtnClicked() {
        val emailVerified = verifyEmail()
        val pswdVerified = verifyPswd()

        if (emailVerified.isNotBlank() && pswdVerified.isNotBlank()){
            prograssBar.visibility = View.VISIBLE
            loginBtn.isEnabled = false
            forgotPswdBtn.isEnabled = false

            auth.signInWithEmailAndPassword(emailVerified, pswdVerified).addOnCompleteListener {
                task ->
                if (task.isSuccessful){
                    prograssBar.visibility = View.GONE
                    loginBtn.isEnabled = true
                    forgotPswdBtn.isEnabled = true
                    Toast.makeText(applicationContext,"Login successful!", Toast.LENGTH_LONG).show()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    prograssBar.visibility = View.GONE
                    loginBtn.isEnabled = true
                    forgotPswdBtn.isEnabled = true
                    Toast.makeText(applicationContext,"Login failed!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun verifyEmail(): String{
        val email: String = emailEditText.text.toString().trim();

        if (email.isBlank()) {
            emailEditText.setError("Please, enter a valid e-mail1!")
            emailEditText.requestFocus()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please, enter a valid e-mail2!")
            emailEditText.requestFocus()
        }
        else return email
        return ""
    }

    private fun verifyPswd(): String{
        val pswd: String = passwordEditText.text.toString().trim();
        if (pswd.isBlank()) {
            passwordEditText.setError("Please, enter a valid password!")
            passwordEditText.requestFocus()
        }
        else return pswd
        return ""
    }

}