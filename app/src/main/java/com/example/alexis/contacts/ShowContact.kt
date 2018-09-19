package com.example.alexis.contacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView


class ShowContact : AppCompatActivity() {

    private fun fillData() {
        val row_id = intent.getIntExtra(EXTRA_ROW_ID, 0)
        val res = dbHelper?.fetchRow(row_id)
        res?.move(1)
        findViewById<TextView>(R.id.textView).text = res?.getString(res.getColumnIndex(DBHelper().KEY_FIRSTNAME))
        findViewById<TextView>(R.id.textView2).text = res?.getString(res.getColumnIndex(DBHelper().KEY_LASTNAME))
        findViewById<TextView>(R.id.textView3).text = res?.getString(res.getColumnIndex(DBHelper().KEY_MOBILE))
        findViewById<TextView>(R.id.textView4).text = res?.getString(res.getColumnIndex(DBHelper().KEY_MAIL))
        findViewById<TextView>(R.id.textView5).text = res?.getString(res.getColumnIndex(DBHelper().KEY_ADDRESS))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_contact)
    }

    override fun onStart() {
        fillData()
        super.onStart()
    }

    fun deleteContact(view: View) {
        val mobile = findViewById<TextView>(R.id.textView3).text.toString()
        dbHelper?.deleteRow(mobile)
        finish()
    }

    fun updateContact(view: View) {
        val first_name = findViewById<TextView>(R.id.textView).text.toString()
        val last_name = findViewById<TextView>(R.id.textView2).text.toString()
        val mobile = findViewById<TextView>(R.id.textView3).text.toString()
        val mail = findViewById<TextView>(R.id.textView4).text.toString()
        val address = findViewById<TextView>(R.id.textView5).text.toString()
        val rowId = dbHelper?.getRowId(mobile)
        val intent = Intent(this, UpdateContact::class.java).apply {
            putExtra(EXTRA_FIRST_NAME, first_name)
            putExtra(EXTRA_LAST_NAME, last_name)
            putExtra(EXTRA_MOBILE, mobile)
            putExtra(EXTRA_MAIL, mail)
            putExtra(EXTRA_ADDRESS, address)
            putExtra(EXTRA_ROW_ID, rowId)
        }
        startActivity(intent)
    }

    fun startCall(view: View) {
        val mobile = findViewById<TextView>(R.id.textView3).text.toString()
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$mobile"))
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),1)

            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent)
            }
        }
        else {
            startActivity(intent)
        }
    }

    fun startMessage(view: View) {
        val mobile = findViewById<TextView>(R.id.textView3).text.toString()
        val intent = Intent(this, SendSms::class.java).apply {
            putExtra(EXTRA_MOBILE, mobile)
        }
        startActivity(intent)
    }
}