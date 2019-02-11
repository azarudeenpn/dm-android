package com.dm.client.ticket

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dm.client.services.EndPoints
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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
                //val timeString = result["requestTime"] as String
                val lat = result["latitude"] as Double
                val lon = result["longitude"] as Double

                //val requestTime = resl
                //val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                //df.timeZone = TimeZone.getTimeZone("IST")
                //val requestTime = df.parse(timeString)

                ui.onSuccess(name, phoneRes, place, lat, lon)
            }
        }, Response.ErrorListener {
            ui.onNetworkError()
        })
        Volley.newRequestQueue(context).add(expandRequest)
    }

    interface Contract {
        fun onNetworkError()
        fun onSuccess(name: String, phone: String, place: String, lat: Double, lon: Double)
    }
}