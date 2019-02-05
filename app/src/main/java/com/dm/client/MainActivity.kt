package com.dm.client

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dm.client.victimregistration.VictimRegisterActivity
import com.dm.client.volunteerregistration.VolunteerRegisterActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun buttonClick(view: View){
        when(view.id){
            R.id.Main_VolunteerButton -> {
                val i = Intent(this, VolunteerRegisterActivity::class.java)
                startActivity(i)
            }
            R.id.Main_RescueButton -> {
                val i = Intent(this, VictimRegisterActivity::class.java)
                startActivity(i)
            }

        }

    }
}
