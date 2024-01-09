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

        auth = Firebase.auth

        emailEditText = findViewById(R.id.emailLoginEditTextEmailAddress)
        passwordEditText = findViewById(R.id.passwordLoginEditTextPassword)
        loginBtn = findViewById(R.id.loginBtn)
        forgotPswdBtn = findViewById(R.id.forgotPswdBtn)
        prograssBar = findViewById(R.id.loginProgressBar)

        val currentUserEmail = intent.getStringExtra("currentUserEmail")
        if (!currentUserEmail.isNullOrEmpty()){
            emailEditText.setText(currentUserEmail)
        }
        val currentUserPswd = intent.getStringExtra("currentUserPswd")
        if (!currentUserPswd.isNullOrEmpty()){
            passwordEditText.setText(currentUserPswd)
            loginBtnClicked()
        }

        loginBtn.setOnClickListener {
            loginBtnClicked()
        }

        forgotPswdBtn.setOnClickListener {
            forgotPswdBtnClicked()
        }

    }

    private fun forgotPswdBtnClicked() {
        val intent = Intent(this, ForgotPswdActivity::class.java)
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
                    Toast.makeText(applicationContext,
                        getString(R.string.login_successful), Toast.LENGTH_LONG).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    prograssBar.visibility = View.GONE
                    loginBtn.isEnabled = true
                    forgotPswdBtn.isEnabled = true
                    Toast.makeText(applicationContext,
                        getString(R.string.login_failed), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun verifyEmail(): String{
        val email: String = emailEditText.text.toString().trim();

        if (email.isBlank()) {
            emailEditText.setError(getString(R.string.please_enter_an_e_mail))
            emailEditText.requestFocus()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.please_enter_a_valid_e_mail))
            emailEditText.requestFocus()
        }
        else return email
        return ""
    }

    private fun verifyPswd(): String{
        val pswd: String = passwordEditText.text.toString().trim();
        if (pswd.isBlank()) {
            passwordEditText.setError(getString(R.string.please_enter_a_valid_password))
            passwordEditText.requestFocus()
        }
        else return pswd
        return ""
    }

}