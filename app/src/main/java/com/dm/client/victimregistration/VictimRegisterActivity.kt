package com.dm.client.victimregistration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dm.client.R
import com.dm.client.victim.VictimActivity

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

    override fun onSuccess(phone: String) {
        val preference = getSharedPreferences("credentials", Context.MODE_PRIVATE)
        preference.edit().apply {
            putBoolean("isVolunteer", false)
            putString("phone", "+91$phone")
        }.apply()
        Toast.makeText(this, "We got your request, a volunteer will contact you soon", Toast.LENGTH_LONG).show()
        val i = Intent(this, VictimActivity::class.java)
        startActivity(i)
    }

    override fun onNoNetwork() {
        Toast.makeText(this, "NO Network, going on P2P Mode", Toast.LENGTH_LONG).show()

    }

    fun victimRegistrationButtonClick(view: View) {
        when (view.id) {
            R.id.VictimSubmitButton -> {
                val preferences = getSharedPreferences("location", Context.MODE_PRIVATE)

                presenter.register(
                    nameInput.text.toString(), manualLocationInput.text.toString(),
                    phoneInput.text.toString(), preferences.getFloat("latitude", 10.053946F)
                    , preferences.getFloat("longitude", 76.379725F)
                )
            }
        }
    }
}
