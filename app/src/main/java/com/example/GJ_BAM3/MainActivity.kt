package com.example.GJ_BAM3

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private val networkStateReceiver = NetworkStateReceiver()

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(networkStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        val readButton: Button = findViewById(R.id.readButton)
        readButton.setOnClickListener {
            getData();
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
}
