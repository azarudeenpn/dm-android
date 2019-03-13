package com.dm.client.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.icu.text.RelativeDateTimeFormatter
import android.os.AsyncTask
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.dm.client.database.Request
import com.dm.client.database.RequestDatabase

class PeerToPeer : Service() {


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val db = RequestDatabase.getInstance(this)

        val task = @SuppressLint("StaticFieldLeak")
        object: AsyncTask<Request, Int, Int>(){

            override fun doInBackground(vararg params: Request?): Int {
                //db?.requestDao()?.insert(params[0])
                val res = db?.requestDao()?.getAll()

                Log.v("dm", res.toString())

                //db?.requestDao()?.deleteAll()
                return 1
            }
        }

        val x = Request(0, "929292929", "Aslam", "Kalamassery", 10f, 76f, "2-SEP-2018")

        task.execute(x)



        return Service.START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {

        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "Peer2Peer Service is destroyed", Toast.LENGTH_LONG).show()
    }
}