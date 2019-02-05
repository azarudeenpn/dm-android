package com.dm.client.victimregistration

import android.content.Context

class VictimRegisterPresenter(val ui: contract,val context: Context) {

    fun register(name: String, location: String) {
        if (name.isBlank() || location.isBlank()) {
            if (name.isBlank()) {
                ui.onNameerror("Name should not empty")
            }

            if (location.isBlank()){
                ui.onLocationerror("Enter Location")
            }
        }
    }

    interface contract{
        fun onNameerror(error: String)
        fun onLocationerror(error: String)
    }
}