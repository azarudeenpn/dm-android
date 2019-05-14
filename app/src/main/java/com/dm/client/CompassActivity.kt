package com.dm.client

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.dm.client.ticket.TicketActivity
import com.dm.client.volunteer.VolunteerActivity
import java.util.*

class CompassActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var compassView: ImageView
    private lateinit var sensorManager: SensorManager

    private var sensor: Sensor? = null

    private var vicPhone = ""
    private var lat = 10f
    private var lon = 76f


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
        Log.v("dm", event!!.values[0].toString())
        compassView.rotation = 360 - event.values[0]
    }

    fun buttonClick(view:View){
        when(view.id){
            R.id.EndTask_Button -> {
                val i = Intent(this, VolunteerActivity::class.java)
                startActivity(i)
            }
            R.id.Compass_PhoneButton -> {
                val i = Intent(Intent.ACTION_DIAL)
                this.vicPhone = intent.getStringExtra("phone")
                i.data = Uri.parse("tel:$vicPhone")
                startActivity(i)
            }
            R.id.Compass_MapButton -> {
                this.lat = intent.getFloatExtra("lat",10f)
                this.lon= intent.getFloatExtra("lon",76f)
                val uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", lat, lon)
                val i = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(i)
            }

        }
    }

}
