package com.dm.client.volunteer

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.dm.client.R
import com.dm.client.VictimTicket

class VolunteerActivity : AppCompatActivity(), VolunteerPresenter.Contract {

    private lateinit var ticketList: RecyclerView
    private lateinit var presenter: VolunteerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer)

        ticketList = findViewById(R.id.Volunteer_VictimListRecycler)
        ticketList.layoutManager = LinearLayoutManager(this)

        presenter = VolunteerPresenter(this, this)

        val preferences = getSharedPreferences("location", Context.MODE_PRIVATE)
        val lat = preferences.getFloat("latitude", 10.054054f)
        val lon = preferences.getFloat("longitude", 76.38014f)
        presenter.getVictimTickets(lat, lon)
    }

    override fun onListReady(list: ArrayList<VictimTicket>) {
        ticketList.adapter = VictimTicketListAdapter(list)
    }
}