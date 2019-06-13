package com.dm.client.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
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
                Log.v("test", "ka")
            }

            val json = JSONObject(msg)

            
        }
    }

}