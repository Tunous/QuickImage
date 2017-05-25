package me.thanel.quickimage.extensions

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import me.thanel.quickimage.R

/**
 * Create a notification which displays uploading message with an indeterminate progress bar.
 */
fun Context.createUploadingNotification(id: Int) =
        createNotification(R.drawable.ic_upload, R.string.image_upload_in_progress)
                .setProgress(0, 0, true)
                .display(this, id)

/**
 * Create a notification which displays error message.
 */
fun Context.createFailedUploadNotification(id: Int) =
        createNotification(R.drawable.ic_alert, R.string.image_upload_fail)
                .display(this, id)

/**
 * Create a notification for a link to uploaded image.
 *
 * It displays the link for the image, opens it in browser when clicked on the notification
 * and contains share button to share the link to other applications.
 *
 * @param link The link to the image.
 */
fun Context.createUploadedNotification(id: Int, link: String, image: Bitmap?) {
    val builder = createNotification(R.drawable.ic_image, R.string.image_upload_success)
            .setContentText(link)

    val linkUri = Uri.parse(link)

    // When clicked on open the image with default application
    val resultIntent = Intent(Intent.ACTION_VIEW, linkUri)
    val resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0)
    builder.setContentIntent(resultPendingIntent)

    // Add a share button
    val shareIntent = createShareLinkIntent(link)
    val sharePendingIntent = PendingIntent.getActivity(this, 0, shareIntent, 0)
    val shareAction = NotificationCompat.Action(R.drawable.ic_share, getString(R.string.share),
            sharePendingIntent)
    builder.addAction(shareAction)

    // Add an image
    image?.let { builder.setLargeIcon(it) }

    builder.display(this, id)
}

fun Context.hideUploadNotification(id: Int) {
    val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(id)
}

private fun Context.createNotification(@DrawableRes smallIcon: Int, @StringRes title: Int) =
        NotificationCompat.Builder(this)
                .setSmallIcon(smallIcon)
                .setContentTitle(getString(title))
                .setColor(ContextCompat.getColor(this, R.color.primary))
                .setAutoCancel(true)

private fun NotificationCompat.Builder.display(context: Context, id: Int) {
    val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(id, build())
}
