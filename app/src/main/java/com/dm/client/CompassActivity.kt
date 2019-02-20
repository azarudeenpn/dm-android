package com.dm.client

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView

class CompassActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var compassView: ImageView
    private lateinit var sensorManager: SensorManager

    private var sensor: Sensor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)


        compassView = findViewById(R.id.Compass_Compass)
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        Log.v("dm",event!!.values[0].toString())
        compassView.rotation = 360 - event.values[0]
    }

}
