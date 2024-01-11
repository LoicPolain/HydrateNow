package be.shylo.hydratenow.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import be.shylo.hydratenow.R
import be.shylo.hydratenow.databinding.ActivityChartBinding
import be.shylo.hydratenow.model.UserViewModel
import be.shylo.hydratenow.model.WaterLogViewModel
import be.shylo.hydratenow.model.data.User
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.charts.Cartesian
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class ChartActivity : DrawerActivity() {
    private lateinit var activityChartBinding: ActivityChartBinding
    private lateinit var chart: AnyChartView
    private lateinit var chartDesign: Cartesian
    private val animationDuration = 1000L
    private lateinit var waterLogViewModel: WaterLogViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        activityChartBinding = ActivityChartBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(activityChartBinding.root)
        addActivityTitle("Daily overview")



        waterLogViewModel = ViewModelProvider(this).get(WaterLogViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        auth = Firebase.auth
        initUserInformation()


        if (isLandscape()) chartDesign = AnyChart.column()
        else chartDesign = AnyChart.vertical()



        chart = findViewById(R.id.horizontalBarChart)

        chartDesign.animation(true).title(getString(R.string.daily_overview))


        lifecycleScope.launch {
            if (!waterLogViewModel.dataChartIsSet){
                waterLogViewModel.dataChartIsSet = true
                val dailyWaterlogs = waterLogViewModel.getWaterLogs(auth.currentUser?.uid.toString()).toList()
                val design = if (isLandscape()) chartDesign.column(dailyWaterlogs) else chartDesign.bar(dailyWaterlogs)
            }
            chart.setChart(chartDesign)
        }
    }

    private fun isLandscape(): Boolean {
        val orientation = resources.configuration.orientation
        chart = findViewById(R.id.horizontalBarChart)
        return orientation == Configuration.ORIENTATION_LANDSCAPE
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
        val user = User(userId, userUsername, userEmail)
        println(user.toString())
        userViewModel.checkUserInDB(user)
    }
}