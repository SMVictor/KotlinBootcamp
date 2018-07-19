package com.practice.project.androidbootcamp.utilities

import android.content.Context
import android.net.ConnectivityManager


class NetworkUtilities {

    fun isConnectedToNetwork(context: Context): Boolean {
        var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}