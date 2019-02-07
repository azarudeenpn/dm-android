package com.dm.client.volunteer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.dm.client.R
import com.dm.client.VictimTicket
import kotlinx.android.synthetic.main.layout_victim_ticket_list_item.view.*
import java.util.*

class VictimTicketListAdapter(val list: ArrayList<VictimTicket>): RecyclerView.Adapter<VictimTicketListAdapter.ViewHolder>(){

    private lateinit var context: Context
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        context = p0.context
        val view = LayoutInflater.from(p0.context).inflate(R.layout.layout_victim_ticket_list_item, p0, false)
        return ViewHolder(view)
    }




    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.distanceView.text = "${list[p1].distance} KM"
        p0.mapButton.setOnClickListener {
            val uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", list[p1].lat, list[p1].lon)
            Log.v("dm", uri)
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            context.startActivity(i)
        }
        p0.phoneButton.setOnClickListener {
            val i = Intent(Intent.ACTION_DIAL)
            i.data = Uri.parse("tel:${list[p1].phone}")
            context.startActivity(i)
        }
    }


    override fun getItemCount() = list.size


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val distanceView = view.findViewById<TextView>(R.id.Volunteer_DistanceView)!!
        val mapButton = view.findViewById<ImageButton>(R.id.Volunteer_ListMapButton)!!
        val phoneButton = view.findViewById<ImageButton>(R.id.Volunteer_ListPhoneButton)!!
    }
}