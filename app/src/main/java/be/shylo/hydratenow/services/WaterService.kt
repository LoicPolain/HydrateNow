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
import java.util.Calendar
import java.util.TimeZone

class WaterService : Service() {

    companion object{
        fun addDrunkWater(drunkWater: Float) {
            drunkAmountWaterLiter += drunkWater
        }

        val targetAmountWaterLiter: Float = 2.0f
        var drunkAmountWaterLiter: Float = 0f;
        private var timeToWaitBtwNotificationsMinutes: Int = 15
            get() = field
            set(value) {
                field = value
            }
        private const val REMINDER_NOTIFICATION_ID = 1
        private const val RESET_NOTIFICATION_ID = 1
    }

    private val notificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }
    private lateinit var notificationBuilder: Builder
    private lateinit var serviceHandler: Handler

    override fun onCreate() {
        super.onCreate()
        notificationBuilder = initStartForeground()
        val handlerThread: HandlerThread = HandlerThread("Watertracking").apply { start() }
        serviceHandler = Handler(handlerThread.looper)
        sendReminderNotification()
        resetMidnight()
    }

    override fun onDestroy() {
        serviceHandler.removeCallbacksAndMessages(null)
    }

    private fun initStartForeground(): Builder {
        val pendingIntent: PendingIntent = getPendingIntent()
        //If >=API 26 then createNotificationChannel Else no need for notification channels since they were introduced in Android 8.0 (API level 26)
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { createNotificationChannel() } else ""
        val notificationBuilder = getNotificationBuilder(pendingIntent, channelId)
        startForeground(REMINDER_NOTIFICATION_ID, notificationBuilder.build())
        startForeground(REMINDER_NOTIFICATION_ID, notificationBuilder.build())
        return notificationBuilder
    }

    private fun sendReminderNotification() {
        val defaultTimeZone: TimeZone = TimeZone.getDefault()
        val calendar = Calendar.getInstance(defaultTimeZone)

        calendar.timeInMillis = System.currentTimeMillis()

        val initTime = calendar.timeInMillis
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+timeToWaitBtwNotificationsMinutes)

        val deadlineTime = calendar.timeInMillis
        val duration = deadlineTime - initTime

        serviceHandler.postDelayed({
            sendReminderNotification()
            notificationManager.cancel(REMINDER_NOTIFICATION_ID)
            notificationBuilder.setContentText(getString(R.string.don_t_forget_to_drink_water_you_have_currently_drunk) + " %.2f".format(drunkAmountWaterLiter) +"L")
            notificationManager.notify(REMINDER_NOTIFICATION_ID, notificationBuilder.build())
        }, duration)
    }

    private fun resetMidnight() {
        val defaultTimeZone: TimeZone = TimeZone.getDefault()
        val calendar = Calendar.getInstance(defaultTimeZone)

        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val initTime = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)+24)
        val deadlineTime = calendar.timeInMillis
        val duration = deadlineTime - initTime

        serviceHandler.postDelayed({
            resetMidnight()
            drunkAmountWaterLiter = 0f
            notificationManager.cancel(RESET_NOTIFICATION_ID)
            notificationBuilder
                .setContentText(getString(R.string.your_water_amount_has_been_reset))
            notificationManager.notify(RESET_NOTIFICATION_ID, notificationBuilder.build())
        }, duration)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O) //Requires at least API 26
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
            .setContentText(getString(R.string.initialized_your_water_tracking_you_have_currently_drunk_2f).format(drunkAmountWaterLiter) + "L")
            .setSmallIcon(R.drawable.baseline_local_drink_24)
            .setContentIntent(pendingIntent)
            .setOngoing(false)
            .setAutoCancel(true)
    }
}