package com.dm.client

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.Preference
import android.view.View

class VictimActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_victim)
    }

    fun logoutButtonClick(view: View) {
        when (view.id) {
            R.id.Victim_logout -> {
                val i = Intent(this, MainActivity::class.java)
                val preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE)
                preferences.edit().clear().apply()
                startActivity(i)
            }

        }
    }
}
