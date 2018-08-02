package com.practice.project.androidbootcamp.utilities

import android.content.Context
import android.net.ConnectivityManager


class NetworkUtilities {

    fun isConnectedToNetwork(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}