package be.shylo.hydratenow.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import be.shylo.hydratenow.R
import com.airbnb.lottie.LottieAnimationView

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val lottieAnimationView: LottieAnimationView = findViewById(R.id.lottieAnimationView)
        lottieAnimationView.animate().translationX(-2000f).setDuration(1000).setStartDelay(2000)

        val textview: TextView = findViewById(R.id.appTitleTextView)
        textview.animate().translationX(2000f).setDuration(1000).setStartDelay(2000)

        val thread = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
                finally {
                    val intent = Intent(this@SplashActivity, LaunchScreenActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        thread.start()
    }
}