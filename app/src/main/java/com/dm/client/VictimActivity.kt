package com.dm.client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

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
