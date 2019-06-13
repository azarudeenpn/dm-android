package com.dm.client.victim

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dm.client.services.EndPoints

class VictimPresenter {

    fun deleteRequest(phone: String, context: Context, ui: Contract){
        val url = Uri.Builder().apply {
            scheme("http")
            encodedAuthority("${EndPoints().hostname}/victim/delete")
            appendQueryParameter("phone", phone)
        }.build()

        val requestDelete = JsonObjectRequest(Request.Method.DELETE, url.toString(), null,
            Response.Listener {res->
                run {
                    val success = res["success"] as Boolean

                    if(success){
                        ui.onSuccess()
                    }
                }
            },
            Response.ErrorListener {
                Toast.makeText(context, "Cannot connect to network", Toast.LENGTH_LONG).show()
            })

        Volley.newRequestQueue(context).add(requestDelete)
    }

    interface Contract{
        fun onSuccess()
    }
}