package me.thanel.quickimage

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
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

fun Context.createUploadingNotification() =
        createNotification(R.drawable.ic_upload, R.string.uploading_image)
                .display(this)

fun Context.createFailedUploadNotification() =
        createNotification(R.drawable.ic_alert, R.string.image_upload_failed)
                .display(this)

fun Context.createUploadedNotification(link: String) {
    val builder = createNotification(R.drawable.ic_image, R.string.image_uploaded)
            .setContentText(link)

    val linkUri = Uri.parse(link)

    // When clicked on open the image with default application
    val resultIntent = Intent(Intent.ACTION_VIEW, linkUri)
    val resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0)
    builder.setContentIntent(resultPendingIntent)

    // Add a share button
    val shareIntent = Intent(Intent.ACTION_SEND, linkUri).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, link)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    val sharePendingIntent = PendingIntent.getActivity(this, 0, shareIntent, 0)
    val shareAction = NotificationCompat.Action(R.drawable.ic_share, getString(R.string.share),
            sharePendingIntent)
    builder.addAction(shareAction)

    builder.display(this)
}

private fun Context.createNotification(@DrawableRes smallIcon: Int, @StringRes title: Int) =
        NotificationCompat.Builder(this)
                .setSmallIcon(smallIcon)
                .setContentTitle(getString(title))
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setAutoCancel(true)

private fun NotificationCompat.Builder.display(context: Context) {
    val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val id = context.getString(R.string.app_name).hashCode()
    notificationManager.notify(id, build())
}
