package be.shylo.hydratenow.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import be.shylo.hydratenow.R
import be.shylo.hydratenow.databinding.ActivityMainBinding

class MainActivity : DrawerActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
    }
}