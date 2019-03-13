package com.dm.client.volunteerregistration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dm.client.R
import com.dm.client.volunteer.VolunteerActivity

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

    override fun onPhoneError(error: String) {
        phoneInput.error = error
    }

    override fun onNameError(error: String) {
        nameInput.error = error
    }

    override fun onNetworkError() {
        Toast.makeText(this, "Network Error", Toast.LENGTH_LONG).show()
    }

    override fun onSuccess(phone: String) {
        val preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE)
        preferences.edit().apply {
            putBoolean("isVolunteer", true)
            putString("phone", "+91$phone")
        }.apply()
        val i = Intent(this, VolunteerActivity::class.java)
        startActivity(i)
    }


    fun volunteerRegistrationButtonClick(view: View) {
        val preferences = getSharedPreferences("location", Context.MODE_PRIVATE)
        val lat = preferences.getFloat("latitude", 10.235684F)
        val lon = preferences.getFloat("longitude", 76.547960F)
        when (view.id) {
            R.id.VolunteerSubmitButton -> {
                presenter.register(nameInput.text.toString(), phoneInput.text.toString(), lat, lon)
            }
        }

    }

}
