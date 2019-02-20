package com.dm.client.ticket

import android.content.Context
import android.net.Uri
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dm.client.services.EndPoints
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class TicketPresenter(private val context: Context, private val ui: Contract) {

    fun expand(phone: String) {

        val url = Uri.Builder().apply {
            scheme("http")
            encodedAuthority("${EndPoints().hostname}/victim/request/expand")
            appendQueryParameter("phone", phone)
        }.toString()
        val expandRequest = StringRequest(url, Response.Listener { res ->
            run {
                val response = JSONObject(res)
                val result = response["result"] as JSONObject
                val name = result["name"] as String
                val phoneRes = result["phone"] as String
                val place = result["location"] as String
                val epochTime = result["requestTime"] as Long
                val lat = result["latitude"] as Double
                val lon = result["longitude"] as Double

                val date = Date(epochTime)

                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
                val dateString = dateFormat.format(date)

                val timeFormat = SimpleDateFormat("hh:mm aa", Locale.ENGLISH)
                val timeString = timeFormat.format(date)

                ui.onSuccess(name, phoneRes, place, lat, lon, dateString, timeString)
            }
        }, Response.ErrorListener {
            ui.onNetworkError()
        })
        Volley.newRequestQueue(context).add(expandRequest)
    }

    fun acceptRequest(volPhone: String, vicPhone: String) {
        val acceptedRequest = object: StringRequest(
            Request.Method.POST,
            "http://${EndPoints().hostname}/victim/request/accept",
            Response.Listener { response ->
                run {
                    val result = JSONObject(response)
                    if(result["success"] as Boolean){
                        ui.onAcceptSuccess()
                    }
                    else{
                        TODO("Error code is not set, might need to delete the SharedPreferences")
                    }
                }

            },
            Response.ErrorListener {
                ui.onNetworkError()
            }){
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["volphone"] = volPhone
                params["vicphone"] = vicPhone
                return params
            }
        }
        Volley.newRequestQueue(context).add(acceptedRequest)
    }

    interface Contract {
        fun onNetworkError()
        fun onSuccess(
            name: String,
            phone: String,
            place: String,
            lat: Double,
            lon: Double,
            dateString: String,
            timeString: String
        )
        fun onAcceptSuccess()
    }
}