package com.dm.client.victim

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dm.client.MainActivity
import com.dm.client.R
import com.dm.client.informationcentre.InformationCentreActivity


class VictimActivity : AppCompatActivity(), VictimPresenter.Contract {


    private lateinit var presenter: VictimPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_victim)


        presenter = VictimPresenter()
    }

    fun buttonClick(view: View) {
        when (view.id) {
            R.id.Victim_logout -> {
                val i = Intent(this, MainActivity::class.java)
                val preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE)
                preferences.edit().clear().apply()
                startActivity(i)
            }
            R.id.Victim_ICButton -> {
                val i = Intent(this, InformationCentreActivity::class.java)
                startActivity(i)
            }
            R.id.Victim_Delete_Button -> {

                val credentials = getSharedPreferences("credentials", Context.MODE_PRIVATE)
                val phone = credentials.getString("phone", null)


                val dialog = AlertDialog.Builder(this, R.style.DialogeTheme)
                    .setTitle("Are you safe? Delete request only if you are safe")
                    .setPositiveButton("Yes") { _, _ ->
                        presenter.deleteRequest(phone, this, this)
                    }
                    .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
                    .create()
                dialog.show()


            }
        }
    }

    override fun onSuccess() {
        Toast.makeText(this, "Your Request has been deleted", Toast.LENGTH_LONG).show()
        val credentials = getSharedPreferences("credentials", Context.MODE_PRIVATE)
        credentials.edit().clear().apply()
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

}
