package com.dm.client

//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.atan2

class Informationcentre : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informationcentre)

        val compass = CompassDirection()
        val angle = compass.calc(11.694640f, 75.820944f, 12.268006f, 75.266447f)
        Toast.makeText(this, angle.toString(), Toast.LENGTH_LONG).show()
    }


}





