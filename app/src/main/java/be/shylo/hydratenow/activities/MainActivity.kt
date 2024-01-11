package be.shylo.hydratenow.activities

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import be.shylo.hydratenow.R
import be.shylo.hydratenow.databinding.ActivityMainBinding
import be.shylo.hydratenow.model.UserViewModel
import be.shylo.hydratenow.model.WaterLogViewModel
import be.shylo.hydratenow.model.data.User
import be.shylo.hydratenow.model.data.WaterLog
import be.shylo.hydratenow.services.WaterService
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Calendar
import java.util.TimeZone
import kotlin.math.roundToInt

class MainActivity : DrawerActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private  lateinit var addWaterBtn: Button
    private lateinit var waterAmountTextView: TextView
    private lateinit var percentTextView: TextView
    private lateinit var watergoalProgressBar: ProgressBar

    private val itemLstWaterAmounts: Array<String> = arrayOf("0,25L", "0,33L", "0,5L", "1L")
    private lateinit var spinner: Spinner
    private lateinit var arrayAdapter: ArrayAdapter<String>

    private var waterAmount: Float = 0.25f

    private lateinit var auth: FirebaseAuth;
    private lateinit var userViewModel: UserViewModel
    private lateinit var waterLogViewModel: WaterLogViewModel
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
        addActivityTitle("Home - HydrateNow")

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        waterLogViewModel = ViewModelProvider(this).get(WaterLogViewModel::class.java)
        auth = Firebase.auth
        initUserInformation()

        lifecycleScope.launch{
            initWaterService()

            WaterService.drunkAmountWaterLiter = waterLogViewModel.getTotalWaterAmount(auth.currentUser?.uid.toString())
            println("hello: " + WaterService.drunkAmountWaterLiter)

            waterAmountTextView = findViewById(R.id.waterAmountLiterTextView)
            waterAmountTextView.setText("%.2f".format(WaterService.drunkAmountWaterLiter) + "L / " + "%.2f".format(WaterService.targetAmountWaterLiter) + "L")

            percentTextView = findViewById(R.id.percentTextView)
            percentTextView.setText("%.2f".format(WaterService.drunkAmountWaterLiter/WaterService.targetAmountWaterLiter*100) + " %")

            watergoalProgressBar = findViewById(R.id.watergoalProgressBar)
            watergoalProgressBar.progress = (WaterService.drunkAmountWaterLiter/WaterService.targetAmountWaterLiter*100).roundToInt()


        }


        initSpinner()

        addWaterBtn = findViewById(R.id.waterBtn)
        addWaterBtn.setOnClickListener {
            updateWaterData()
            addWaterLogToDB()
        }
    }

    private fun addWaterLogToDB() {
        val defaultTimeZone: TimeZone = TimeZone.getDefault()
        val calendar = Calendar.getInstance(defaultTimeZone)
        val userId = auth.currentUser?.uid.toString()
        val waterLog = WaterLog(0, waterAmount, calendar.timeInMillis, userId)
        waterLogViewModel.addWaterLog(waterLog)
    }

    private fun initUserInformation() {
        if (auth.currentUser == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        val userId: String = auth.currentUser?.uid.toString()
        val userUsername: String = ""
        val userEmail: String = auth.currentUser?.email.toString()
        user = User(userId, userUsername, userEmail)
        userViewModel.checkUserInDB(user)
    }

    private fun initSpinner() {
        spinner = findViewById(R.id.waterAmountSpinner)
        arrayAdapter  = ArrayAdapter(this, R.layout.lst_item, itemLstWaterAmounts)
        spinner.setAdapter(arrayAdapter)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(itemLstWaterAmounts.get(position)){
                    itemLstWaterAmounts.get(0) -> waterAmount = 0.25f
                    itemLstWaterAmounts.get(1) -> waterAmount = 0.33f
                    itemLstWaterAmounts.get(2) -> waterAmount = 0.5f
                    itemLstWaterAmounts.get(3) -> waterAmount = 1f
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    private fun updateWaterData() {
        launchWaterService(waterAmount)
        waterAmountTextView.setText("%.2f".format(WaterService.drunkAmountWaterLiter) + "L / " + "%.2f".format(WaterService.targetAmountWaterLiter) + "L")
        watergoalProgressBar.progress = (WaterService.drunkAmountWaterLiter/WaterService.targetAmountWaterLiter*100).roundToInt()
        percentTextView.setText("%.2f".format(WaterService.drunkAmountWaterLiter/WaterService.targetAmountWaterLiter*100) + " %")
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
        //serviceIntent.putExtra("addedWater", drunkWater)
        WaterService.addDrunkWater(drunkWater)
        ContextCompat.startForegroundService(this, serviceIntent)
    }
}