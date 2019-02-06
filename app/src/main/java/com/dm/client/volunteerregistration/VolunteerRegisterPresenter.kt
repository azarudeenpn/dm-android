package com.dm.client.volunteerregistration

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class VolunteerRegisterPresenter(val ui: Contract, val context: Context) {

    fun register(name: String, phone: String) {
        if (name.isBlank() || phone.isBlank()) {
            if (name.isBlank()) {
                ui.onNameerror("Name should not empty")

            }
            if (phone.isBlank()) {
                ui.onPhoneerror("Phone number should not empty")

            }
        } else {
            val registerRequest = object :
                StringRequest(
                    Request.Method.POST,
                    "http://192.168.0.3:8000/volunteer/register",
                    Response.Listener { response ->
                        run {
                            val reader = JSONObject(response)
                            if (reader.getBoolean("success")) {

                                Toast.makeText(context, "Details entered in Database", Toast.LENGTH_LONG).show()
                            } else {
                                val type: Int = reader.getInt("type")
                                when (type) {
                                    101 -> ui.onPhoneerror("Incorrect phone number")
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
                    params["lat"] = "10"
                    params["lon"] = "76"
                    return params
                }

            }


            Volley.newRequestQueue(context).add(registerRequest)
        }
    }

    interface Contract {
        fun onPhoneerror(error: String)
        fun onNameerror(error: String)
        fun onNetworkError()
    }
}