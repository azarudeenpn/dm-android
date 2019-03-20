package com.dm.client.informationcentre

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dm.client.services.EndPoints
import org.json.JSONArray
import org.json.JSONObject

class InformationCentrePresenter(private val context: Context, private val ui: Contract) {

    fun getNews() {
        val news = StringRequest("http://${EndPoints().hostname}/panel/news", Response.Listener { response ->
            run {

                val res = JSONObject(response)
                if (res.getBoolean("success")) {
                    val result = res["result"] as JSONArray
                    val list = ArrayList<NewsItem>()
                    for (i in 0 until result.length()) {

                        val jasonItem = result.getJSONObject(i)
                        val item = NewsItem(
                            jasonItem.getInt("priority"),
                            jasonItem.getString("creationTime"),
                            jasonItem.getString("heading"),
                            jasonItem.getString("body")

                        )
                        list.add(item)
                    }
                    ui.onListReady(list)
                }

                //Toast.makeText(context, response, Toast.LENGTH_LONG).show()
            }

        }, Response.ErrorListener {
            Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show()
        })
        Volley.newRequestQueue(context).add(news)
    }

    interface Contract {
        fun onListReady(List: ArrayList<NewsItem>)
    }

}