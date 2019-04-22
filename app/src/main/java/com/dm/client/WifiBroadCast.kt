package com.dm.client

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import android.widget.Toast

class WifiBroadCast(
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val activity: MainActivity
) : BroadcastReceiver() {


    private val list = mutableListOf<WifiP2pDevice>()

    private val peerListListener = WifiP2pManager.PeerListListener { peerList ->


        list.clear()
        list.addAll(peerList.deviceList)


        for (i in 0 until list.size) {
            Log.v("dm", list[i].deviceName)

            val config = WifiP2pConfig().apply {
                deviceAddress = list[i].deviceAddress
            }

            manager.connect(channel, config, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Toast.makeText(activity, "Connected", Toast.LENGTH_LONG).show()
                }

                override fun onFailure(reason: Int) {

                }

            })
        }

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                activity.wifiNotEnabled(state, WifiP2pManager.WIFI_P2P_STATE_ENABLED)

            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                manager.requestPeers(channel, peerListListener)
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                manager.requestConnectionInfo(channel) {

                }

            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                Toast.makeText(activity, "THIS DEVICE CHANGED ACTION", Toast.LENGTH_LONG).show()
            }

        }
    }
}