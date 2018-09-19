package com.example.alexis.contacts

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDateTime


class DBHelper {
    val KEY_FIRSTNAME: String = "first_name"
    val KEY_LASTNAME: String = "last_name"
    val KEY_MOBILE: String = "mobile"
    val KEY_ADDRESS: String = "address"
    val KEY_MAIL: String = "email"
    val KEY_ROW_ID = "_id"
    val PROJECTION = arrayOf(
            KEY_ROW_ID,
            KEY_FIRSTNAME,
            KEY_LASTNAME,
            KEY_MOBILE,
            KEY_MAIL,
            KEY_ADDRESS
    )

    private val TABLE_NAME = "mycontacts"
    private val DATABASE_NAME = "contactDB"

    private val DATABASE_VERSION = 1

    private val TABLE_CREATE = (
            "create table " + TABLE_NAME + "(" +
                    KEY_ROW_ID + " integer primary key autoincrement," +
                    KEY_FIRSTNAME + " text," +
                    KEY_LASTNAME + " text," +
                    KEY_MOBILE + " text," +
                    KEY_ADDRESS + " text," +
                    KEY_MAIL + " text)")

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"

    class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DBHelper().DATABASE_NAME, null, DBHelper().DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(DBHelper().TABLE_CREATE)
        }
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(DBHelper().SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }
    }
    var dbHelper :FeedReaderDbHelper? = null

    fun DBInstanciation(context: Context) {
        this.dbHelper = FeedReaderDbHelper(context)
    }

    fun createRow(first_name: String?, last_name: String?,  mobile: String?, mail: String, address: String?) {
        val db = dbHelper?.writableDatabase
        val values = ContentValues().apply {
            put(KEY_FIRSTNAME, first_name)
            put(KEY_LASTNAME, last_name)
            put(KEY_ADDRESS, address)
            put(KEY_MOBILE, mobile)
            put(KEY_MAIL, mail)
        }
        db?.insert(TABLE_NAME, null, values)
    }

    fun updateRow(rowId: Int, first_name: String?, last_name: String?,  mobile: String?, mail: String?, address: String?) {
        val db = dbHelper?.writableDatabase
        val values = ContentValues().apply {
            put(KEY_FIRSTNAME, first_name)
            put(KEY_LASTNAME, last_name)
            put(KEY_ADDRESS, address)
            put(KEY_MOBILE, mobile)
            put(KEY_MAIL, mail)
        }
        db?.update(TABLE_NAME, values, "$KEY_ROW_ID = ?", arrayOf(rowId.toString()))
    }

    fun deleteRow(mobile : String) {
        val db = dbHelper?.writableDatabase
        // Define 'where' part of query.
        val selection = "$KEY_MOBILE LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(mobile)
        // Issue SQL statement.
        val deletedRows = db?.delete(TABLE_NAME, selection, selectionArgs)
    }

    fun deleteAll() {
        val db = dbHelper?.writableDatabase
        db?.delete(TABLE_NAME, null, null)
    }

    fun fetchRow(rowId: Int): Cursor {
        val db = dbHelper?.readableDatabase
        val selection = "$KEY_ROW_ID = ?"
        val selectionArg = arrayOf(rowId.toString())
        val result = db?.query(TABLE_NAME, PROJECTION,
                selection, selectionArg, null, null, null)
        if (result == null) {
            throw SQLException("No note matching ID: $rowId")
        }
        return result
    }

    fun fetchRow(mobile: String): Cursor {
        val db = dbHelper?.readableDatabase
        val selection = "$KEY_MOBILE =?"
        val selectionArg = arrayOf(mobile)
        val result = db?.query(TABLE_NAME, PROJECTION,
                selection, selectionArg, null, null, null)
        if (result == null) {
            throw SQLException("No note matching ID: $mobile")
        }
        return result
    }

    fun fetchAllRows(): Cursor? {
        val db = dbHelper?.readableDatabase
        val res = db?.query(TABLE_NAME, PROJECTION, null, null, null, null, null)
        return res
    }

    fun getRowId(mobile : String) : Int? {
        val db = dbHelper?.readableDatabase
        val selection = "$KEY_MOBILE =?"
        val selectionArg = arrayOf(mobile)
        val res = db?.query(TABLE_NAME, arrayOf(KEY_ROW_ID), selection, selectionArg, null, null, null)
        res?.moveToFirst()
        val result = res?.getInt(res.getColumnIndex(KEY_ROW_ID))
        res?.close()
        return result
    }


    val KEY_SMS_CONTACT_ID: String = "contact_id"
    val KEY_SMS_CONTENT: String = "content"
    val KEY_SMS_TIME: String = "reception_time"
    val KEY_SMS_ROW_ID = "_id"
    val SMS_PROJECTION = arrayOf(
            KEY_SMS_ROW_ID,
            KEY_SMS_CONTACT_ID,
            KEY_SMS_CONTENT,
            KEY_SMS_TIME
    )

    private val SMS_TABLE_NAME = "mysms"

    private val SMS_TABLE_CREATE = (
            "create table " + TABLE_NAME + "(" +
                    KEY_SMS_ROW_ID + " integer primary key autoincrement," +
                    KEY_SMS_CONTACT_ID + " int," +
                    KEY_SMS_CONTENT + " text," +
                    KEY_SMS_TIME + " text)")

    private val SMS_SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"

    class SmsDbHelper(context: Context) : SQLiteOpenHelper(context, DBHelper().DATABASE_NAME, null, DBHelper().DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(DBHelper().SMS_TABLE_CREATE)
        }
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(DBHelper().SMS_SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }
    }
    var smsDbHelper :SmsDbHelper? = null

    fun smsDBInstanciation(context: Context) {
        this.smsDbHelper = SmsDbHelper(context)
    }

    fun smsCreateRow(contactId: Int, content: String) {
        val db = dbHelper?.writableDatabase
        val values = ContentValues().apply {
            put(KEY_SMS_CONTACT_ID, contactId)
            put(KEY_SMS_CONTENT, content)
            put(KEY_SMS_TIME, java.util.Calendar.getInstance().time.toString())
        }
        db?.insert(TABLE_NAME, null, values)
    }

    fun smsGetContact(contactId: Int) : Cursor? {
        val db = dbHelper?.readableDatabase
        val selection = "$KEY_SMS_CONTACT_ID =?"
        val selectionArg = arrayOf(contactId.toString())
        val result = db?.query(SMS_TABLE_NAME, SMS_PROJECTION,
                selection, selectionArg, null, null, null)
        if (result == null) {
            throw SQLException("No note matching ID: $contactId")
        }
        return result
    }
}