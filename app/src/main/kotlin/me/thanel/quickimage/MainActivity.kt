package me.thanel.quickimage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import me.thanel.quickimage.extensions.copyTextToClipboard
import me.thanel.quickimage.uploader.ImageUploader
import me.thanel.quickimage.uploader.imgur.ImgurImageUploader

class MainActivity : AppCompatActivity(), View.OnClickListener, ImageUploader.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uploadButton.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != REQUEST_PICK_FILE) return
        if (resultCode != Activity.RESULT_OK) return

        val imageUri = data?.data ?: return
        uploadImage(imageUri)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.uploadButton -> chooseImage()
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_PICK_FILE)
    }

    private fun uploadImage(imageUri: Uri) {
        if (!ExternalStoragePermissionActivity.askForPermission(this, imageUri)) return

        ImgurImageUploader(this, this).uploadImage(imageUri)
    }

    override fun onSuccess(link: String) {
        copyTextToClipboard("Image link", link)
    }

    override fun onFailure() {
        Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_PICK_FILE = 1001
    }
}
