package com.dm.client

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import android.widget.Toast

class WifiBroadCast(private val manager: WifiP2pManager, private val channel: WifiP2pManager.Channel, private val activity: MainActivity): BroadcastReceiver() {

    private val peers = mutableListOf<WifiP2pDevice>()

    private val peerListListerner = WifiP2pManager.PeerListListener {peerList ->
        Log.v("dm","Devices********************\n${peerList.deviceList.toString()}")
        peers.addAll(peerList.deviceList)

        val device = peers[0]

        val config = WifiP2pConfig().apply {
            deviceAddress = device.deviceAddress
            wps.setup = WpsInfo.PBC
        }


        manager.connect(channel, config, object :WifiP2pManager.ActionListener{
            override fun onSuccess() {
                Toast.makeText(activity, "Connected", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(reason: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                activity.wifiNotEnabled(state, WifiP2pManager.WIFI_P2P_STATE_ENABLED)

            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                Toast.makeText(activity, "PEERS CHANGED ACTION", Toast.LENGTH_LONG).show()
                manager.requestPeers(channel, peerListListerner)
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                Toast.makeText(activity, "CONNECTION CHANGED ACTION", Toast.LENGTH_LONG).show()
            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                Toast.makeText(activity, "THIS DEVICE CHANGED ACTION", Toast.LENGTH_LONG).show()
            }

        }
    }
}