package com.example.ckcitlabroom.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

object NetworkUtils {

    fun isNetworkAvailable(context: Context): Boolean {
        return try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                    ?: return true // Nếu không get được ConnectivityManager, mặc định là có internet

            val network = connectivityManager.activeNetwork
            if (network == null) {
                Log.d("NetworkUtils", "No active network")
                return false
            }

            val capabilities = connectivityManager.getNetworkCapabilities(network)
            if (capabilities == null) {
                Log.d("NetworkUtils", "Network capabilities is null")
                return false
            }

            val hasInternet =
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            val hasWifi = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            val hasCellular = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            val hasEthernet = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)

            val isConnected = hasWifi || hasCellular || hasEthernet

            Log.d("NetworkUtils", "Network available: $isConnected, has internet: $hasInternet")
            return isConnected && hasInternet
        } catch (e: Exception) {
            Log.e("NetworkUtils", "Error checking network", e)
            return false
        }
    }
}