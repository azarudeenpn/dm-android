package com.dm.client.victimregistration

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.EditText
import com.dm.client.R
import kotlinx.android.synthetic.main.activity_victim_register.*

class VictimRegisterActivity : AppCompatActivity(), VictimRegisterPresenter.contract {

    private lateinit var nameInput: EditText
    private lateinit var manualLocationInput: EditText

    private lateinit var presenter: VictimRegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_victim_register)

        nameInput = findViewById(R.id.VictimRegister_NameInput)
        manualLocationInput = findViewById(R.id.VictimRegister_LocationInput)

        presenter = VictimRegisterPresenter(this, this)
    }

    override fun onNameerror(error: String) {
        nameInput.error = error
    }

    override fun onLocationerror(error: String) {
        manualLocationInput.error = error
    }

    fun victimRegistrationButtonClick(view: View) {
        when (view.id) {
            R.id.VictimSubmitButton -> {
                presenter.register(nameInput.text.toString(), manualLocationInput.text.toString())
            }
        }
    }

}
