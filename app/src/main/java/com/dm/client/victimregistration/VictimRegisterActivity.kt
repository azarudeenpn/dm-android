package com.dm.client.victimregistration

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.EditText
import com.dm.client.R
import kotlinx.android.synthetic.main.activity_victim_register.*

class VictimRegisterActivity : AppCompatActivity(), VictimRegisterPresenter.Contract {

    private lateinit var nameInput: EditText
    private lateinit var manualLocationInput: EditText
    private lateinit var phoneInput: EditText

    private lateinit var presenter: VictimRegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_victim_register)

        nameInput = findViewById(R.id.VictimRegister_NameInput)
        manualLocationInput = findViewById(R.id.VictimRegister_LocationInput)
        phoneInput = findViewById(R.id.VictimRegister_PhoneInput)

        presenter = VictimRegisterPresenter(this, this)
    }

    override fun onNameError(error: String) {
        nameInput.error = error
    }

    override fun onLocationError(error: String) {
        manualLocationInput.error = error
    }

    override fun onPhoneError(error: String) {
        phoneInput.error = error
    }

    fun victimRegistrationButtonClick(view: View) {
        when (view.id) {
            R.id.VictimSubmitButton -> {
                val preferences = getSharedPreferences("location", Context.MODE_PRIVATE)

                presenter.register(
                    nameInput.text.toString(), manualLocationInput.text.toString(),
                    phoneInput.text.toString(), preferences.getFloat("latitude", 10f)
                    , preferences.getFloat("longitude", 76f)
                )
            }
        }
    }
}
