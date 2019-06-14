package com.dm.client.victimregistration

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dm.client.database.RequestDatabase
import com.dm.client.services.EndPoints
import org.json.JSONArray
import org.json.JSONObject
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class VictimRegisterPresenter(private val ui: Contract, private val context: Context) {

    fun register(name: String, location: String, phone: String, lat: Float, lon: Float) {
        if (name.isBlank() || location.isBlank() || phone.isBlank()) {
            if (name.isBlank()) {
                ui.onNameError("Name should not empty")
            }

            if (location.isBlank()) {
                ui.onLocationError("Enter Location")
            }
            if (phone.isBlank()) {
                ui.onPhoneError("Phone is required")
            }
        } else {
            //Everything is OK

            val victimTicketRequest = object : StringRequest(
                Method.POST,
                "http://${EndPoints().hostname}/victim/ticket/create",
                Response.Listener { response ->
                    run {
                        val result = JSONObject(response)
                        if (result.getBoolean("success")) {
                            Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
                            ui.onSuccess(phone)
                        } else {
                            when (result.getInt("type")) {
                                101 -> ui.onPhoneError("Phone number is invalid")
                            }
                        }
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(context, "P2P Mode is enabled", Toast.LENGTH_LONG).show()
                    val db = RequestDatabase.getInstance(context)


                    val creation = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"))
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val formatted = creation.format(formatter)
                    val req = com.dm.client.database.Request("+91$phone",name,location,lat,lon,formatted)
                    val task = @SuppressLint("StaticFieldLeak")
                    object : AsyncTask<com.dm.client.database.Request, Int, Int>() {

                        override fun doInBackground(vararg params: com.dm.client.database.Request?): Int {
                            db?.requestDao()?.insert(req)
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
                                SmsManager.getDefault().sendMultipartTextMessage("+918089709076", null, string, null, null);
                            }
                            return 1
                        }

                    }
                    task.execute(req)
                   Log.v("dm","deb")
                }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()

                    params["name"] = name
                    params["phone"] = "+91$phone"
                    params["location"] = location
                    params["lat"] = lat.toString()
                    params["lon"] = lon.toString()

                    return params
                }
            }
            Volley.newRequestQueue(context).add(victimTicketRequest)
        }
    }

    interface Contract {
        fun onNameError(error: String)
        fun onLocationError(error: String)
        fun onPhoneError(error: String)
        fun onSuccess(phone: String)
        fun onNoNetwork()
    }
}