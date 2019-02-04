package com.dm.client.volunteerregistration

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

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
                StringRequest(Request.Method.POST, "http://192.168.0.3:8000/volunteer/register", Response.Listener {

                }, Response.ErrorListener {
                    ui.onNetworkError()

                }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["name"] = name
                    params["phone"] = phone
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