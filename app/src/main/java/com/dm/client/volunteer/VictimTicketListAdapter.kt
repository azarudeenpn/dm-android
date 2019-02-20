package com.dm.client.volunteer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.dm.client.R
import com.dm.client.data.VictimTicket
import com.dm.client.ticket.TicketActivity
import java.util.*

class VictimTicketListAdapter(private val list: ArrayList<VictimTicket>) :
    RecyclerView.Adapter<VictimTicketListAdapter.ViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        context = p0.context
        val view = LayoutInflater.from(p0.context).inflate(R.layout.layout_victim_ticket_list_item, p0, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.distanceView.text = "${list[p1].distance} KM"
        p0.timeDurationView.text = "Requested ${list[p1].timeDuration} minutes ago"
        p0.mapButton.setOnClickListener {
            val uri =
                String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", list[p1].lat, list[p1].lon)
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


    //Inner class to access the object of the outer class
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val distanceView = view.findViewById<TextView>(R.id.Volunteer_DistanceView)!!
        val mapButton = view.findViewById<ImageButton>(R.id.Volunteer_ListMapButton)!!
        val phoneButton = view.findViewById<ImageButton>(R.id.Volunteer_ListPhoneButton)!!
        val timeDurationView = view.findViewById<TextView>(R.id.Volunteer_TimeView)!!

        init {
            view.setOnClickListener {
                val i = Intent(view.context, TicketActivity::class.java)
                i.putExtra("phone", list[layoutPosition].phone)
                view.context.startActivity(i)
            }
        }
    }
}