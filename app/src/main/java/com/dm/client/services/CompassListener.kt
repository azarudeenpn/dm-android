package com.dm.client.services

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.dm.client.database.Request
import com.dm.client.database.RequestDatabase
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CompassListener: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent!!.extras

        if(bundle != null){

            val pdus = bundle.get("pdus") as Array<*>

            var msg = ""
            for(i in 0 until pdus.size){
                val sms = SmsMessage.createFromPdu(pdus[i] as ByteArray , SmsMessage.FORMAT_3GPP)

                msg += sms.messageBody
            }

            Log.v("test", msg)
            try {
                val json = JSONArray(msg)
                val db = RequestDatabase.getInstance(context!!)

                val task = @SuppressLint("StaticFieldLeak")
                object : AsyncTask<Request, Int, Int>() {

                    override fun doInBackground(vararg params: Request?): Int {
                        for(i in 0 until json.length()){
                            val jsonObject = json.getJSONObject(i)
                            val item = Request(jsonObject["phone"] as String,
                                jsonObject["name"] as String,
                                jsonObject["place"] as String,
                                (jsonObject["latitude"] as Double).toFloat(),
                                (jsonObject["longitude"] as Double).toFloat(),
                                jsonObject["creationTime"] as String
                            )
                            db?.requestDao()?.insert(item)
                        }
                        return 1
                    }

                }
                task.execute(null)


            }
            catch (e: JSONException){
            }

        }
    }

}