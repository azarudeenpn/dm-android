package com.dm.client.informationcentre

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dm.client.CompassDirection
import com.dm.client.R

class InformationCentreActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informationcentre)

        val compass = CompassDirection()
        val angle = compass.calc(11.694640f, 75.820944f, 12.268006f, 75.266447f)
        Toast.makeText(this, angle.toString(), Toast.LENGTH_LONG).show()
    }


}





