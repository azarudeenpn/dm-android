package com.dm.client

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dm.client.informationcentre.InformationCentreActivity
import com.dm.client.services.PeerToPeer
import com.dm.client.victimregistration.VictimRegisterActivity
import com.dm.client.volunteerregistration.VolunteerRegisterActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {

    private val PERMISSIONREQUEST = 1251
    private val LOCATIONSETTINGREQUEST = 2213

    private lateinit var locationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationClient = LocationServices.getFusedLocationProviderClient(this)

        val i = Intent(this, PeerToPeer::class.java)
        startService(i)
        //Checking weather location is enabled.
        if (isLocationEnabled()) {
            getLocation()
        } else
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

        val locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates = 10
            interval = 5000
            fastestInterval = 2500

        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            //everything is OK
            locationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    for (location in locationResult.locations) {
                        Log.v("dm", location.toString())
                        Toast.makeText(this@MainActivity, location.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }, null)
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this, LOCATIONSETTINGREQUEST)
                } catch (sendEx: IntentSender.SendIntentException) {
                    //Never mind.
                    Log.v("dm", "hajf")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            LOCATIONSETTINGREQUEST -> {
                getLocation()
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
                val i = Intent(this, InformationCentreActivity::class.java)
                startActivity(i)
            }
        }

    }

}
