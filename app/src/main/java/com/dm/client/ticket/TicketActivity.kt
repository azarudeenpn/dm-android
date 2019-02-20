package com.dm.client.ticket

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.dm.client.CompassActivity
import com.dm.client.R
import java.util.*


class TicketActivity : AppCompatActivity(), TicketPresenter.Contract {

    private lateinit var presenter: TicketPresenter
    private lateinit var nameView: TextView
    private lateinit var placeView: TextView
    private lateinit var dateView: TextView
    private lateinit var timeView: TextView

    private var lat = 10f
    private var lon = 76f

    private var vicPhone = ""
    private var volPhone = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)

        nameView = findViewById(R.id.Ticket_NameView)
        placeView = findViewById(R.id.Ticket_PlaceView)
        dateView = findViewById(R.id.Ticket_DateView)
        timeView = findViewById(R.id.Ticket_TimeView)

        presenter = TicketPresenter(this, this)
        this.vicPhone = intent.getStringExtra("phone")

        val preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE)
        this.volPhone = preferences.getString("phone", null)!!

        presenter.expand(this.vicPhone)
    }

    override fun onNetworkError() {
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show()
    }


    override fun onSuccess(name: String, phone: String, place: String, lat: Double, lon: Double, dateString: String, timeString: String) {
        nameView.text = name
        placeView.text = place
        timeView.text = timeString
        dateView.text = dateString
        this.lat = lat.toFloat()
        this.lon = lon.toFloat()
        this.vicPhone = phone

    }

    override fun onAcceptSuccess() {
        val i = Intent(this, CompassActivity::class.java)
        startActivity(i)
    }

    fun buttonClick(view: View) {
        when (view.id) {
            R.id.Ticket_MapButton -> {
                val uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", lat, lon)
                val i = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(i)
            }
            R.id.Ticket_PhoneButton -> {
                val i = Intent(Intent.ACTION_DIAL)
                i.data = Uri.parse("tel:$vicPhone")
                startActivity(i)
            }
            R.id.Ticket_ProceedButton -> {
                Toast.makeText(this, "Proceed Button is clicked", Toast.LENGTH_LONG).show()
                presenter.acceptRequest(volPhone, vicPhone)
            }
        }
    }

}
