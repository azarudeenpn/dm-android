package com.dm.client.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast

class PeerToPeer : Service() {


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Toast.makeText(this, "Testing Service", Toast.LENGTH_SHORT).show()

        Log.v("dm", "Background Service")

        return Service.START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {

        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "Peer2Peer Service is destroyed", Toast.LENGTH_LONG).show()
    }
}