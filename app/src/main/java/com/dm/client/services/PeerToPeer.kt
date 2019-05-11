package com.dm.client.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dm.client.database.RequestDatabase
import com.peak.salut.Callbacks.SalutCallback
import com.peak.salut.Callbacks.SalutDataCallback
import com.peak.salut.Salut
import com.peak.salut.SalutDataReceiver
import com.peak.salut.SalutServiceData
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.*

class PeerToPeer : Service(), SalutDataCallback {


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val db = RequestDatabase.getInstance(this)

        //For getting the
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val res = db?.requestDao()?.getAll()

                if (res?.isNotEmpty()!!) {

                    val jsonArr = JSONArray()

                    for (i in 0 until res.size) {
                        val item = JSONObject()
                        item.put("name", res[i].name)
                        item.put("creationTime", res[i].creationTime)
                        item.put("latitude", res[i].latitude)
                        item.put("longitude", res[i].longitude)
                        item.put("phone", res[i].phone)
                        item.put("place", res[i].place)
                        jsonArr.put(item)
                    }

                    val uploadAllRequest = object : JsonObjectRequest(
                        Method.POST,
                        "http://${EndPoints().hostname}/victim/p2prequests",
                        null,
                        Response.Listener { response ->
                            run {
                                if (response["success"] as Boolean) {
                                    Toast.makeText(this@PeerToPeer, "Uploaded the data", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        Response.ErrorListener {
                        }) {
                        override fun getBody(): ByteArray {
                            return jsonArr.toString().toByteArray()
                        }

                        override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {


                            val strRes = String(response?.data!!, StandardCharsets.UTF_8)
                            val jsonRes = JSONObject(strRes)
                            if (jsonRes["success"] as Boolean) {
                                db.requestDao().deleteAll()
                            }
                            return super.parseNetworkResponse(response)
                        }
                    }
                    Volley.newRequestQueue(this@PeerToPeer).add(uploadAllRequest)
                }
            }
        }, 0, 1000 * 30)

        val dataReceiver = SalutDataReceiver(this, this)
        val serviceData = SalutServiceData("disaster", 50902, "management")

        val network = P2pSalut(dataReceiver, serviceData, SalutCallback {
            Toast.makeText(this@PeerToPeer, "This device does not support Peer2Peer", Toast.LENGTH_LONG).show()
        })

        network.startNetworkService {
            //When a device is connected
            Toast.makeText(this@PeerToPeer, it.macAddress, Toast.LENGTH_LONG).show()

        }

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {

        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "Peer2Peer Service is destroyed", Toast.LENGTH_LONG).show()
    }

    //for Receiving the data
    override fun onDataReceived(data: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class P2pSalut(dataReceiver: SalutDataReceiver, serviceData: SalutServiceData, deviceNotSupported: SalutCallback) :
        Salut(dataReceiver, serviceData, deviceNotSupported) {
        override fun serialize(o: Any?): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

}