package be.shylo.hydratenow.activities

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import be.shylo.hydratenow.R
import be.shylo.hydratenow.databinding.ActivityMainBinding
import be.shylo.hydratenow.services.WaterService

class MainActivity : DrawerActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private  lateinit var addWaterBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
        addActivityTitle("Home - HydrateNow")

        addWaterBtn = findViewById(R.id.waterBtn)
        addWaterBtn.setOnClickListener {
            launchWaterService(0.25f)
        }

        initWaterService()
    }

    private fun initWaterService(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            launchWaterService()
        }

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){granted ->
            if (granted) launchWaterService()
            else {
                requestPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
        }

        when{
            checkSelfPermission(POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                launchWaterService()
            }
            shouldShowRequestPermissionRationale(POST_NOTIFICATIONS) -> {
                requestPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
            else -> requestPermissionLauncher.launch(POST_NOTIFICATIONS)
        }
    }


    private fun launchWaterService(drunkWater: Float = 0f) {
        val serviceIntent = Intent(this, WaterService::class.java)
        serviceIntent.putExtra("addedWater", drunkWater)
        WaterService.addDrunkWater(drunkWater)
        ContextCompat.startForegroundService(this, serviceIntent)
    }
}