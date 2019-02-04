package com.dm.client.volunteerregistration

class VolunteerRegisterPresenter(val ui: Contract) {

    fun register(name: String, phone: String) {
        if (name.isBlank() || phone.isBlank()) {
            if (name.isBlank()) {
                ui.onNameerror("Name should not empty")

            }
            if (phone.isBlank()) {
                ui.onPhoneerror("Phone number should not empty")

            }
        }
    }

    interface Contract {
        fun onPhoneerror(error: String)
        fun onNameerror(error: String)
    }
}