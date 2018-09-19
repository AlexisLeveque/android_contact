package com.example.alexis.contacts

import android.content.Intent

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.contact_row.view.*

const val EXTRA_FIRST_NAME = "com.example.contact.FIRST_NAME"
const val EXTRA_LAST_NAME = "com.example.contact.LAST_NAME"
const val EXTRA_MOBILE = "com.example.contact.MOBILE"
const val EXTRA_MAIL = "com.example.contact.MAIL"
const val EXTRA_ADDRESS = "com.example.contact.ADDRESS"
const val EXTRA_ROW_ID = "com.example.contact.NBR"
var dbHelper: DBHelper? = null


class Contact : AppCompatActivity() {
    private var stopDate: String? = null


    private var listView :ListView? = null

    public fun fillData() {
        val c = dbHelper?.fetchAllRows()
        listView?.emptyView = findViewById(android.R.id.empty)
        val adapter = SimpleCursorAdapter(this,
                R.layout.contact_row, c, arrayOf(DBHelper().KEY_FIRSTNAME, DBHelper().KEY_MOBILE), intArrayOf(R.id.name, R.id.phonenumber))
        listView?.adapter = adapter
    }

//    private mMessageClickedHandler : OnItemClickListener = new OnItemClickListener() {
//        public void onItemClick(AdapterView parent, View v, int position, long id) {
//            // Do something in response to the click
//        }
//    }


    override fun onStart() {
        fillData()
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        dbHelper = DBHelper()
        dbHelper?.DBInstanciation(this)
        dbHelper?.smsDBInstanciation(this)
        listView = findViewById(android.R.id.list)
        fillData()

        listView?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val mobile = view.phonenumber.text.toString()
            val intent = Intent(this, ShowContact::class.java).apply {
                putExtra(EXTRA_MOBILE, mobile)
                putExtra(EXTRA_ROW_ID, dbHelper?.getRowId(mobile))
            }
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_color, menu)
        return true
    }

    fun addContact(view: View) {
        val intent = Intent(this, AddContact::class.java)
        startActivity(intent)
    }

    override fun onStop() {
        stopDate = java.util.Calendar.getInstance().time.toLocaleString()
        super.onStop()
    }

    override fun onRestart() {
        if (stopDate != null) {
            Toast.makeText(applicationContext,"Application closed at $stopDate",Toast.LENGTH_SHORT).show()
            println(stopDate)
            stopDate = null
        }
        super.onRestart()
    }

}
