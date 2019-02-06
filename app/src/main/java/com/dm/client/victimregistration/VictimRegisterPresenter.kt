package com.dm.client.victimregistration

import android.content.Context

class VictimRegisterPresenter(val ui: contract, val context: Context) {

    fun register(name: String, location: String) {
        if (name.isBlank() || location.isBlank()) {
            if (name.isBlank()) {
                ui.onNameError("Name should not empty")
            }

            if (location.isBlank()) {
                ui.onLocationError("Enter Location")
            }
        }
    }

    interface contract {
        fun onNameError(error: String)
        fun onLocationError(error: String)
    }
}