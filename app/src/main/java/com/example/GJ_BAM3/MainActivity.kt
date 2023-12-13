package com.example.GJ_BAM3

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {
    private val networkStateReceiver = NetworkStateReceiver()
    private val  READ_CONTACTS_PERMISSION_REQUEST_CODE = 5;
    private val  READ_CALENDAR_PERMISSION_REQUEST_CODE = 3;
    @SuppressLint("Range", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(networkStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        val readButton: Button = findViewById(R.id.readButton)
        readButton.setOnClickListener {
            getData();
        }

        val readContacts: Button = findViewById(R.id.readContacts)
        readContacts.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS") != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf("android.permission.READ_CONTACTS"),
                    READ_CONTACTS_PERMISSION_REQUEST_CODE
                )
            } else {
                readContacts();
            }
        }

        val setWallpaper: Button = findViewById(R.id.setWallpaper)
        setWallpaper.setOnClickListener {
            val myWallpaperManager = WallpaperManager.getInstance(
                applicationContext
            )
            try {
                myWallpaperManager.setResource(R.drawable.cat1) //<--My app just set my app icon image as wallpaper, this is not I wanted. I wanted to set my selected image as wallpaper
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        val readCalendar: Button = findViewById(R.id.readCalendar)
        readCalendar.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this, "android.permission.READ_CALENDAR") != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf("android.permission.READ_CALENDAR"),
                    READ_CALENDAR_PERMISSION_REQUEST_CODE
                )
            } else {
                readCalendar();
            }
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getData() {
        val url = URL("https://jsonplaceholder.typicode.com/posts")
        GlobalScope.launch {
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET" // optional default is GET
                inputStream.bufferedReader().use {
                    it.lines().forEach { line -> Log.d("ACT", line)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkStateReceiver)
    }

    @SuppressLint("Range")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_CONTACTS_PERMISSION_REQUEST_CODE) {
            if (
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                readContacts();
            } else {
                Log.d("PermissionInfo", "Permission denied");
            }
        }
        if (requestCode == READ_CALENDAR_PERMISSION_REQUEST_CODE) {
            if (
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                readCalendar();
            } else {
                Log.d("PermissionInfo", "Permission denied");
            }
        }
    }

    @SuppressLint("Range")
    private fun readContacts() {
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        while (cursor!!.moveToNext()) {
            val contactId =
                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
            val displayName =
                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            Log.d("Reading contacts", "Contact ${contactId} ${displayName}")
        }
    }

    @SuppressLint("Range")
    private fun readCalendar() {
        Log.d("Calendar", "Reading...");
        val cursor = contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            null,
            null,
            null,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " ASC"
        )
        while (cursor!!.moveToNext()) {
            val calendarTimeZone =
                cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_TIME_ZONE))
            val color =
                cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR))
            val displayName =
                cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME))
            Log.d("Reading calendar", "Calendar ${displayName} ${calendarTimeZone} ${color}")
        }
    }
}
