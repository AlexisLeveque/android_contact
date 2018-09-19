package com.example.alexis.contacts

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText

class AddContact : Activity() {
    private val DIALOG_ERROR_PHONE = "This is not a phone number"
    private val DIALOG_ERROR_DUP = "There is already a contact with this Number"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_contact)
    }

    fun save(view: View) {
        val first_name = findViewById<EditText>(R.id.editText).text.toString()
        val last_name = findViewById<EditText>(R.id.editText2).text.toString()
        val mobile = findViewById<EditText>(R.id.editText3).text.toString()
        val email = findViewById<EditText>(R.id.editText4).text.toString()
        val address = findViewById<EditText>(R.id.editText5).text.toString()
        if (mobile_check(mobile)) {
            dbHelper?.createRow(first_name, last_name, mobile, email, address)
            finish()

        }
    }

    private fun mobile_check(mobile : String) : Boolean {
        val builder = AlertDialog.Builder(this)
        builder.setPositiveButton("Cancel") {_,_ ->
        }

        if (!mobile.matches(Regex("\\+?[0-9]+"))) {
            builder.setMessage(DIALOG_ERROR_PHONE)
            val dialog: AlertDialog = builder.create()
            dialog.show()
            return false
        }
        val test = dbHelper?.fetchRow(mobile)

        if (test != null) {
            if (test.moveToFirst()) {
                builder.setMessage(DIALOG_ERROR_DUP)
                val dialog: AlertDialog = builder.create()
                dialog.show()
                test.close()
                return false
            }
            test.close()
        }
        return true
    }
}