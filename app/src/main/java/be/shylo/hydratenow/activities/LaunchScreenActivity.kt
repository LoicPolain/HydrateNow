package be.shylo.hydratenow.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import be.shylo.hydratenow.R

class LaunchScreenActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_screen)

        btnLogin = findViewById(R.id.btn_login)
        btnRegister = findViewById(R.id.btn_register)

        btnLogin.setOnClickListener{
            loginBtnClicked()
        }

        btnRegister.setOnClickListener{
            registerBtnClicked()
        }
    }

    private fun loginBtnClicked(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun registerBtnClicked(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}