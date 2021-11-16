package com.example.gps

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class LocationService: Service() {
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent!!.getBooleanExtra("isLocating", true)) {
            LocationHelper.getInstance().startLocationListening(this.applicationContext, sendNotification)
        } else {
            stopService(intent)
            LocationHelper.getInstance().stopLocating()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private val sendNotification: (Location)->Unit = ::sendLocation

    private fun sendLocation(l: Location) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("id", "Channel", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, "id")

        val notification = builder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_btn_speak_now)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setSubText("Ваша геопозиция передается")
            .setContentText(l.latitude.toString() + " " + l.longitude.toString())
            .build()
        startForeground(200, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}