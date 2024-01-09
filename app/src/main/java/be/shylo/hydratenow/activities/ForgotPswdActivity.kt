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

class ForgotPswdActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private  lateinit var forgotPswdBtn: Button
    private  lateinit var prograssBar: ProgressBar

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pswd)

        emailEditText = findViewById(R.id.recoverEmailEditTextEmailAddress)
        forgotPswdBtn = findViewById(R.id.recoverBtn)
        prograssBar = findViewById(R.id.forgotPswdprogressBar)

        auth = Firebase.auth

        val currentUserEmail: String = intent.getStringExtra("currentUserEmail").toString()

        if (currentUserEmail.isNotBlank()){
            emailEditText.setText(currentUserEmail)
        }

        forgotPswdBtn.setOnClickListener {
            forgotPswdBtnClicked()
        }
    }

    private fun forgotPswdBtnClicked() {
        val verifiedEmail = verifyEmail()
        if (verifiedEmail.isNotBlank()){
            prograssBar.visibility = View.VISIBLE
            forgotPswdBtn.isEnabled = false
            auth.sendPasswordResetEmail(verifiedEmail).addOnCompleteListener {
                task ->
                if (task.isSuccessful){
                    prograssBar.visibility = View.GONE
                    forgotPswdBtn.isEnabled = true
                    Toast.makeText(applicationContext,
                        getString(R.string.recovery_e_mail_has_been_send), Toast.LENGTH_LONG).show()

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.putExtra("currentUserEmail", verifiedEmail)
                    startActivity(intent)
                    finish()
                }
                else {
                    prograssBar.visibility = View.GONE
                    forgotPswdBtn.isEnabled = true
                    Toast.makeText(applicationContext,
                        getString(R.string.recovery_failed), Toast.LENGTH_LONG).show()
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
}