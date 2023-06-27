package com.example.callsimulationapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class DoubleTapListener(private val onDoubleTap: () -> Unit) : SensorEventListener {

    private var lastTapTime = 0L
    private val tapThreshold = 2000 // Milliseconds
    private val forceThreshold = 10.7f // Adjust this value based on your testing

    // Variables to store accelerometer and gyroscope data
    private var accelValues = FloatArray(3)
    private var gyroValues = FloatArray(3)

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // You can handle changes in sensor accuracy here, if necessary
    }

    override fun onSensorChanged(event: SensorEvent) {
        // Store accelerometer and gyroscope data separately
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                accelValues = event.values.clone()
            }
            Sensor.TYPE_GYROSCOPE -> {
                gyroValues = event.values.clone()
            }
        }

        // Calculate gForce using accelerometer data
        val x = accelValues[0]
        val y = accelValues[1]
        val z = accelValues[2]

        val gForce = Math.sqrt((x * x + y * y + z * z).toDouble())

        // Check if the gForce exceeds the forceThreshold
        if (gForce > forceThreshold) {
            // You can use the gyroValues to get more information about the device's orientation
            // and rotation. Analyze the data and determine if it corresponds to a double-tap event
            // on the back of the device.

            // Example: Check if the z-axis rotation is within a certain range
            // Note: You'll need to test and adjust the values for your specific use case
            val gyroZ = gyroValues[2]
            if (gyroZ > -1 && gyroZ < 1) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastTapTime < tapThreshold) {
                    onDoubleTap()
                }
                lastTapTime = currentTime
            }
        }
    }
}
