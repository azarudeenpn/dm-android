package com.dm.client.volunteerregistration

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.dm.client.R

class VolunteerRegisterActivity : AppCompatActivity(), VolunteerRegisterPresenter.Contract {

    private lateinit var nameInput: EditText
    private lateinit var phoneInput: EditText

    private lateinit var presenter: VolunteerRegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_register)

        nameInput = findViewById(R.id.VolunteerRegister_NameInput)
        phoneInput = findViewById(R.id.VolunteerRegister_PhoneInput)

        presenter = VolunteerRegisterPresenter(this, this)

    }

    override fun onPhoneerror(error: String) {
        phoneInput.error = error
    }

    override fun onNameerror(error: String) {
        nameInput.error = error
    }

    override fun onNetworkError() {
        Toast.makeText(this, "Network Error", Toast.LENGTH_LONG).show()
    }


    fun volunteerRegistrationButtonClick(view: View) {
        val preferances = getSharedPreferences("location", Context.MODE_PRIVATE)
        val lat = preferances.getFloat("latitude", 10F)
        val lon = preferances.getFloat("longitude", 76F)
        when (view.id) {
            R.id.VolunteerSubmitButton -> {
                presenter.register(nameInput.text.toString(), phoneInput.text.toString(), lat, lon)

            }
        }


    }


}
