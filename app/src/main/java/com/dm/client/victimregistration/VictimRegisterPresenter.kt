package com.dm.client.victimregistration

import android.content.Context
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dm.client.services.EndPoints
import org.json.JSONObject

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
                    Toast.makeText(context, "Unable to connect to the network", Toast.LENGTH_LONG).show()
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
    }
}