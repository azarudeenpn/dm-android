package com.dm.client

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.provider.Telephony
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bluelinelabs.logansquare.LoganSquare
import com.dm.client.compass.CompassActivity
import com.dm.client.database.Request
import com.dm.client.database.RequestDatabase
import com.dm.client.informationcentre.InformationCentreActivity
import com.dm.client.services.CompassListener
import com.dm.client.services.PeerToPeer
import com.dm.client.victim.VictimActivity
import com.dm.client.victimregistration.VictimRegisterActivity
import com.dm.client.volunteer.VolunteerActivity
import com.dm.client.volunteerregistration.VolunteerRegisterActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.peak.salut.Callbacks.SalutCallback
import com.peak.salut.Callbacks.SalutDataCallback
import com.peak.salut.Callbacks.SalutDeviceCallback
import com.peak.salut.Salut
import com.peak.salut.SalutDataReceiver
import com.peak.salut.SalutServiceData
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(), SalutDataCallback {


    private val PERMISSIONREQUEST = 1251
    private val LOCATIONSETTINGREQUEST = 2213

    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var br: CompassListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val credentials = getSharedPreferences("credentials", Context.MODE_PRIVATE)
        if (credentials.contains("isVolunteer")) {
            if (credentials.getBoolean("isVolunteer", false)) {

                if (credentials.contains("isAccepted")) {
                    if (credentials.getBoolean("isAccepted", false)) {
                        val i = Intent(this@MainActivity, CompassActivity::class.java)
                        startActivity(i)
                    } else {
                        val i = Intent(this@MainActivity, VolunteerActivity::class.java)
                        startActivity(i)
                    }

                } else {
                    val i = Intent(this@MainActivity, VolunteerActivity::class.java)
                    startActivity(i)
                }
            } else {
                val i = Intent(this@MainActivity, VictimActivity::class.java)
                startActivity(i)
            }
        }

        locationClient = LocationServices.getFusedLocationProviderClient(this)


        val i = Intent(this, PeerToPeer::class.java)
        startService(i)
        //Checking weather location is enabled.
        if (isLocationEnabled()) {
            getLocation()
        } else
            openPermissionPrompt()


        val filter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        br = CompassListener()

        //  sendMessage()

        registerReceiver(br, filter)



        val dataReceiver = SalutDataReceiver(this, this)
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID).substring(10, 16)
        val serviceData = SalutServiceData("dis", 2421, deviceId)
        val network = PeerToPeer.P2pSalut(dataReceiver, serviceData, SalutCallback {
            Toast.makeText(this, "This device does not support Peer2Peer", Toast.LENGTH_LONG).show()
        })

/*           network.startNetworkService({
               Toast.makeText(this, "Device connected with address ${it.macAddress}", Toast.LENGTH_LONG).show()
               network.sendToDevice(it, "Testing the pee2peer service") {
                   Log.v("dm", "Unable to send the data")
               }
           }, {
               Toast.makeText(this, "Network Service Started", Toast.LENGTH_LONG).show()
           }, {
               Toast.makeText(this, "Cannot Start Network Service", Toast.LENGTH_LONG).show()
           })

        network.discoverNetworkServices(SalutDeviceCallback {
            Toast.makeText(this, "Found a device", Toast.LENGTH_LONG).show()

            network.registerWithHost(it, {
                Toast.makeText(this, "Device Connected", Toast.LENGTH_LONG).show()
            }, {
                Toast.makeText(this, "Device Not Connected", Toast.LENGTH_LONG).show()
            })
        }, true)*/

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
//                        Toast.makeText(this@MainActivity, location.toString(), Toast.LENGTH_SHORT).show()
                        val preferences = getSharedPreferences("location", Context.MODE_PRIVATE)
                        preferences.edit().apply {
                            putFloat("latitude", location.latitude.toFloat())
                            putFloat("longitude", location.longitude.toFloat())
                        }.apply()
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


    public override fun onResume() {
        super.onResume()

    }

    override fun onDataReceived(data: Any?) {
        Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        unregisterReceiver(br)
        super.onStop()
    }

    fun sendMessage(){

        val db = RequestDatabase.getInstance(this)



        val task = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<Request, Int, Int>() {

            override fun doInBackground(vararg params: Request?): Int {
                val res = db?.requestDao()?.getAll()

                if (res?.isNotEmpty()!!) {

                    val jsonArr = JSONArray()

                    for (i in 0 until res.size) {
                        val item = JSONObject()
                        item.put("name", res[i].name)
                        item.put("creationTime", res[i].creationTime)
                        item.put("latitude", res[i].latitude)
                        item.put("longitude", res[i].longitude)
                        item.put("phone", res[i].phone)
                        item.put("place", res[i].place)
                        jsonArr.put(item)
                    }

                    val smsManager = SmsManager.getDefault()
                    val string = smsManager.divideMessage(jsonArr.toString())
                    SmsManager.getDefault().sendMultipartTextMessage("+919633116645", null, string, null, null);
                }
                return 1
            }

        }
        task.execute(null)

    }

    class p2pSolut(
        dataReceiver: SalutDataReceiver,
        salutServiceData: SalutServiceData,
        deviceNotSupported: SalutCallback
    ) : Salut(dataReceiver, salutServiceData, deviceNotSupported) {

        override fun serialize(o: Any?): String {
            return LoganSquare.serialize(0)
        }

    }
}
