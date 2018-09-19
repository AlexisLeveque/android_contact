package com.example.alexis.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.telephony.SmsManager
import android.view.MenuItem


class SendSms : AppCompatActivity() {
    fun fetchInbox(number: String): ArrayList<String> {
        val sms  = ArrayList<String>()
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS),1)

        val uriSms = Uri.parse("content://sms/inbox")
        val cursor = applicationContext.contentResolver.query(uriSms, arrayOf("_id", "address", "date", "body"), "address =?", arrayOf(number), "date")

        cursor!!.moveToFirst()
        while (cursor.moveToNext()) {
            val address = cursor.getString(1)
            val body = cursor.getString(3)

//            println("======&gt; Mobile number =&gt; $address")
//            println("=====&gt; SMS Text =&gt; $body")

            sms.add("Address=&gt; " + address + "n SMS =&gt; " + body)
        }
        cursor.close()
        return sms

    }

    fun fill_data() {
        val cr = applicationContext.contentResolver
        println("en cours")
        val mobile = intent.getStringExtra(EXTRA_MOBILE).replace(Regex("^0"), "+33")
        fetchInbox(mobile)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.send_sms)
        fill_data()
    }

    fun send(view: View) {
        val mobile = intent.getStringExtra(EXTRA_MOBILE)
        val message =  findViewById<TextView>(R.id.editText).text.toString()
        val smsManager = SmsManager.getDefault()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS),1)

            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                smsManager.sendTextMessage(mobile, null, message, null, null)
            }
        }
        else {
            smsManager.sendTextMessage(mobile, null, message, null, null)
        }
        findViewById<TextView>(R.id.editText).text = ""
        fill_data()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}