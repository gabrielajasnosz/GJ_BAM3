package com.example.GJ_BAM3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log

class NetworkStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("Receiver", "is invoked!")
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        Log.d("Connection info", "Is connected: ${networkInfo?.isConnected}")
        Log.d("Network info:", networkInfo.toString());
        Log.d("Connection info", "Type: ${networkInfo?.type} ${ConnectivityManager.TYPE_WIFI} ")
    }
}