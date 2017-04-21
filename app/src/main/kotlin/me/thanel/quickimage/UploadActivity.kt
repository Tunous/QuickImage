package me.thanel.quickimage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import me.thanel.quickimage.uploader.imgur.ImgurImageUploader

/**
 * An activity without UI that is used to receive shared images and upload them to default service.

 * On success the link to the uploaded image is displayed in a notification and optionally copied to
 * the user's clipboard.
 * On failure a notification with error message is displayed instead.
 */
class UploadActivity : Activity() {
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent ?: return finish()
        val action = intent.action ?: return finish()
        val type = intent.type ?: return finish()

        if (action == Intent.ACTION_SEND) {
            if (type.startsWith("image/")) {
                handleSendImage(intent)
            }
        }

        finish()
    }

    private fun handleSendImage(intent: Intent) {
        imageUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM) ?: return

        if (!ExternalStoragePermissionActivity.askForPermission(this, imageUri)) return

        ImgurImageUploader(this).uploadImage(imageUri!!)
    }

    companion object {
        fun getUploadImageIntent(context: Context, imageUri: Uri): Intent {
            return Intent(context, UploadActivity::class.java).apply {
                action = Intent.ACTION_SEND
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, imageUri)
            }
        }
    }
}
