package me.thanel.quickimage

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import okhttp3.internal.connection.RouteException

/**
 * Tells whether the user is connected to interned.
 */
val Context.isConnected: Boolean
    get() = try {
        val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        connectivityManager?.activeNetworkInfo?.isConnected ?: false
    } catch (ex: RouteException) {
        false
    }

fun Context.copyTextToClipboard(label: String, text: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.primaryClip = ClipData.newPlainText(label, text)
}
