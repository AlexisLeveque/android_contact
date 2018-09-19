package com.example.alexis.contacts

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.EditText

class UpdateContact : AppCompatActivity() {
    private val DIALOG_ERROR_PHONE = "This is not a phone number"
    private val DIALOG_ERROR_DUP = "There is already a contact with this Number"
    private fun fillData() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_contact)
        findViewById<EditText>(R.id.editText).setText(intent.getStringExtra(EXTRA_FIRST_NAME))
        findViewById<EditText>(R.id.editText2).setText(intent.getStringExtra(EXTRA_LAST_NAME))
        findViewById<EditText>(R.id.editText3).setText(intent.getStringExtra(EXTRA_MOBILE))
        findViewById<EditText>(R.id.editText4).setText(intent.getStringExtra(EXTRA_MAIL))
        findViewById<EditText>(R.id.editText5).setText(intent.getStringExtra(EXTRA_ADDRESS))

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    fun save(view: View) {
        val first_name = findViewById<EditText>(R.id.editText).text.toString()
        val last_name = findViewById<EditText>(R.id.editText2).text.toString()
        val mobile = findViewById<EditText>(R.id.editText3).text.toString()
        val email = findViewById<EditText>(R.id.editText4).text.toString()
        val address = findViewById<EditText>(R.id.editText5).text.toString()
        val rowId = intent.getIntExtra(EXTRA_ROW_ID, 0)
        if (intent.getStringExtra(EXTRA_MOBILE) != mobile) {
            if (mobile_check(mobile)) {
                dbHelper?.updateRow(rowId, first_name, last_name, mobile, email, address)
                finish()
            }
        }
        else {
            dbHelper?.updateRow(rowId, first_name, last_name, mobile, email, address)
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