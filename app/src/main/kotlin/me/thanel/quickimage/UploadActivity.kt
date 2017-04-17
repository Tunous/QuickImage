package me.thanel.quickimage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.thanel.quickimage.uploader.ImageUploader
import me.thanel.quickimage.uploader.imgur.ImgurImageUploader

/**
 * An activity without UI that is used to receive shared images and upload them to default service.

 * On success the link to the uploaded image is displayed in a notification and optionally copied to
 * the user's clipboard.
 * On failure a notification with error message is displayed instead.
 */
class UploadActivity : AppCompatActivity(), ImageUploader.Callback {
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
        val imageUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM) ?: return

        ImgurImageUploader(this, this).uploadImage(imageUri)
    }

    override fun onSuccess(link: String) {
    }

    override fun onFailure() {
    }
}
