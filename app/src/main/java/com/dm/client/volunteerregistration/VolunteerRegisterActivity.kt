package com.dm.client.volunteerregistration

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
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

        presenter = VolunteerRegisterPresenter(this)

    }

    override fun onPhoneerror(error: String) {
        phoneInput.error = error
    }

    override fun onNameerror(error: String) {
        nameInput.error = error
    }

    fun volunteerRegistrationButtonClick(view: View) {
        when (view.id) {
            R.id.VolunteerSubmitButton -> {
                presenter.register(nameInput.text.toString(), phoneInput.text.toString())
            }
        }


    }


}
