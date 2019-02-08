package com.dm.client.volunteerregistration

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dm.client.services.EndPoints
import org.json.JSONObject

class VolunteerRegisterPresenter(private val ui: Contract, private val context: Context) {

    fun register(name: String, phone: String, lat: Float, lon: Float) {
        if (name.isBlank() || phone.isBlank()) {
            if (name.isBlank()) {
                ui.onNameError("Name should not empty")

            }
            if (phone.isBlank()) {
                ui.onPhoneError("Phone number should not empty")

            }
        } else {
            val registerRequest = object :
                StringRequest(
                    Request.Method.POST,
                    "http://${EndPoints().hostname}/volunteer/register",
                    Response.Listener { response ->
                        run {
                            val reader = JSONObject(response)
                            if (reader.getBoolean("success")) {

                                Toast.makeText(context, "Details entered in Database", Toast.LENGTH_LONG).show()
                                ui.onSuccess(phone)
                            } else {
                                val type: Int = reader.getInt("type")
                                when (type) {
                                    101 -> ui.onPhoneError("Incorrect phone number")
                                }
                            }
                        }

                    },
                    Response.ErrorListener {
                        ui.onNetworkError()

                    }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["name"] = name
                    params["phone"] = "+91$phone"
                    params["lat"] = lat.toString()
                    params["lon"] = lon.toString()
                    return params
                }
            }


            Volley.newRequestQueue(context).add(registerRequest)
        }
    }

    interface Contract {
        fun onPhoneError(error: String)
        fun onNameError(error: String)
        fun onNetworkError()
        fun onSuccess(phone: String)
    }
}