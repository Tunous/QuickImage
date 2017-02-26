package me.thanel.quickimage

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.internal.connection.RouteException

val Context.isConnected: Boolean
    get() = try {
        val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        connectivityManager?.activeNetworkInfo?.isConnected ?: false
    } catch (ex: RouteException) {
        false
    }
