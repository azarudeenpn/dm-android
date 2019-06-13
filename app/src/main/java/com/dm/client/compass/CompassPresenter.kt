package com.dm.client.compass

import android.content.Context
import android.net.Uri
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dm.client.services.EndPoints

class CompassPresenter(val context: Context, val ui: Contract) {

    fun deleteAccept(volPhone: String, vicPhone: String){

        val url = Uri.Builder().apply {
            scheme("http")
            encodedAuthority("${EndPoints().hostname}/victim/remove")
            appendQueryParameter("vicphone", vicPhone)
            appendQueryParameter("volphone", volPhone)
        }.build()

        val deleteRequest = JsonObjectRequest(Request.Method.DELETE, url.toString(), null,
            Response.Listener { response ->
                run {
                    val success = response["success"] as Boolean
                    if(success)
                        ui.onSuccess()
                }
            },
            Response.ErrorListener {  })

        Volley.newRequestQueue(context).add(deleteRequest)
    }

    interface Contract{
        fun onSuccess()
    }
}