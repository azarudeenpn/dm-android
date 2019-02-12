package com.dm.client

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.dm.client.victimregistration.VictimRegisterActivity
import com.dm.client.volunteer.VolunteerActivity
import com.dm.client.volunteerregistration.VolunteerRegisterActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private val PERMISSIONREQUEST = 1251

    private lateinit var locationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationClient = LocationServices.getFusedLocationProviderClient(this)
        //Checking weather location is enabled.
        if (isLocationEnabled())
            getLocation()
        else
            openPermissionPrompt()
    }


    private fun isLocationEnabled(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            }
            return false
        }
        return false
    }

    private fun openPermissionPrompt() {
        //When never again is given.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            //Show prompt here when the checkmark is checked.
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSIONREQUEST
            )
        }
    }

    //Removed the checking of lint.
    //Currently only last location is used. modify this to use getting location updates.
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationClient.lastLocation.addOnSuccessListener { location ->
            run {
                val preferences = getSharedPreferences("location", Context.MODE_PRIVATE)
                if (location != null) {
                    //Written the location to android preference
                    preferences.edit().apply {
                        putFloat("latitude", location.latitude.toFloat())
                        putFloat("longitude", location.longitude.toFloat())
                    }.apply()
                } else {
                    preferences.edit().apply {
                        putFloat("latitude", 10F)
                        putFloat("longitude", 76F)
                    }.apply()
                }
                val credentials = getSharedPreferences("credentials", Context.MODE_PRIVATE)
                if(credentials.contains("isVolunteer")){
                    if(credentials.getBoolean("isVolunteer", false)) {
                        Toast.makeText(this, "Volunteer is registered", Toast.LENGTH_LONG).show()
                        val i = Intent(this, VolunteerActivity::class.java)
                        startActivity(i)
                    }
                    else{
                        Toast.makeText(this, "Victim is registered", Toast.LENGTH_LONG).show()
                        val i = Intent(this, VictimActivity::class.java)
                        startActivity(i)
                    }
                }
                else{
                    Toast.makeText(this, "No one is registered in this device", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONREQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission is granted.
                    getLocation()
                } else {
                    Toast.makeText(this, "App cannot function without permission", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun buttonClick(view: View) {
        when (view.id) {
            R.id.Main_VolunteerButton -> {
                val i = Intent(this, VolunteerRegisterActivity::class.java)
                startActivity(i)
            }
            R.id.Main_RescueButton -> {
                val i = Intent(this, VictimRegisterActivity::class.java)
                startActivity(i)
            }
            R.id.Main_ICButton -> {
                val i = Intent(this, VolunteerActivity::class.java)
                startActivity(i)
            }
        }

    }

}
