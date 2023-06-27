package com.example.callsimulationapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class DoubleTapService : Service() {

    private lateinit var sensorManager: SensorManager
    private lateinit var doubleTapListener: DoubleTapListener
    private var hasOpenedActivity = false

    private val CHANNEL_ID = "DoubleTapServiceChannel"

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Double Tap Service")
            .setContentText("Tap twice on the back of your phone to trigger a fake call.")
            .setSmallIcon(R.drawable.ic_launcher_background) // Replace with your app's notification icon
            .build()

        startForeground(1, notification)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Initialize the DoubleTapListener with a lambda function to perform the desired action
        doubleTapListener = DoubleTapListener {
            // Perform the desired action when a double-tap is detected

            // Start the FakeCallActivity
            if (!hasOpenedActivity) {
                val intent = Intent(this@DoubleTapService, FakeCallActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                hasOpenedActivity = true
            }

        }

        sensorManager.registerListener(
            doubleTapListener,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        sensorManager.registerListener(
            doubleTapListener,
            sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        Log.d("ThaerOutput", "Service created")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Double Tap Service"
            val descriptionText = "This service detects double taps on the back of the phone."
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(doubleTapListener)
        Log.d("ThaerOutput", "Service created")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        hasOpenedActivity = false
        stopSelf()
    }

}
