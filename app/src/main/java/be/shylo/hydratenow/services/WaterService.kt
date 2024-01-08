package be.shylo.hydratenow.services


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Builder
import be.shylo.hydratenow.R
import be.shylo.hydratenow.activities.MainActivity

class WaterService : Service() {

    companion object{
        fun addDrunkWater(drunkWater: Float) {
            drunkAmountWaterLiter += drunkWater
        }
        private var drunkAmountWaterLiter: Float = 5f;
        private const val NOTIFICATION_ID = 1
    }

    private val notificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var serviceHandler: Handler

    override fun onCreate() {
        super.onCreate()


        notificationBuilder = initStartForeground()
        val handlerThread: HandlerThread = HandlerThread("Watertracking").apply { start() }
        serviceHandler = Handler(handlerThread.looper)
        sendReminderNotification()
    }

    override fun onDestroy() {
        serviceHandler.removeCallbacksAndMessages(null)
    }

    private fun initStartForeground(): NotificationCompat.Builder {
        val pendingIntent: PendingIntent = getPendingIntent()
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { createNotificationChannel() } else ""
        val notificationBuilder = getNotificationBuilder(pendingIntent, channelId)
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
        return notificationBuilder
    }

    private fun sendReminderNotification() {
        serviceHandler.postDelayed({
            sendReminderNotification()
            notificationBuilder.setContentText("Don't forget to drink water! Your have currently drunk %.2f".format(drunkAmountWaterLiter))
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        }, 5000L)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun addAmountDrunkWaterLiter(addedAmountDrunkWaterLiter: Float){
        drunkAmountWaterLiter += addedAmountDrunkWaterLiter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String{
        val channelId: String = "HydrateNow"
        val channelName: String = "HydrateNow"
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        return channelId
    }

    private fun getPendingIntent(): PendingIntent{
        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE else 0
        return PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), flag)
    }

    private fun getNotificationBuilder(pendingIntent: PendingIntent, channelId: String): Builder {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Amount of water drunk")
            .setContentText("Water")
            .setSmallIcon(R.drawable.baseline_local_drink_24)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
    }
}