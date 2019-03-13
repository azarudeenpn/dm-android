package com.dm.client.volunteer

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dm.client.data.VictimTicket
import com.dm.client.services.EndPoints
import org.json.JSONArray
import org.json.JSONObject

class VolunteerPresenter(private val context: Context, private val ui: Contract) {

    fun getVictimTickets(lat: Float, lon: Float) {

        val url = Uri.Builder().apply {
            scheme("http")
            encodedAuthority("${EndPoints().hostname}/victim/requests")
            appendQueryParameter("lat", lat.toString())
            appendQueryParameter("lon", lon.toString())
        }.build().toString()
        val victimTicketRequest = StringRequest(url, Response.Listener { response ->
            run {
                Log.v("dm", response)
                val res = JSONObject(response)
                if (res.getBoolean("success")) {
                    val result = res["result"] as JSONArray
                    val list = ArrayList<VictimTicket>()
                    for (i in 0 until result.length()) {
                        val jsonItem = result.getJSONObject(i)
                        val item = VictimTicket(
                            jsonItem.getString("name"),
                            jsonItem.getString("phone"),
                            jsonItem.getDouble("latitude").toFloat(),
                            jsonItem.getDouble("longitude").toFloat(),
                            jsonItem.getDouble("distance").toFloat(),
                            jsonItem.getInt("timeTaken")

                        )
                        list.add(item)
                    }
                    ui.onListReady(list)
                }
            }

        }, Response.ErrorListener {
            Toast.makeText(context, "Unable to connect to network", Toast.LENGTH_LONG).show()
        })
        Volley.newRequestQueue(context).add(victimTicketRequest)
    }

    interface Contract {
        fun onListReady(list: ArrayList<VictimTicket>)
    }
}