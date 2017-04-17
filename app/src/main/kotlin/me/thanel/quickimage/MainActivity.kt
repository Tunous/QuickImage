package me.thanel.quickimage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import me.thanel.quickimage.extensions.copyTextToClipboard
import me.thanel.quickimage.uploader.ImageUploader
import me.thanel.quickimage.uploader.imgur.ImgurImageUploader

class MainActivity : AppCompatActivity(), View.OnClickListener, ImageUploader.Callback {
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        image.setOnClickListener(this)
        uploadButton.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != REQUEST_PICK_FILE) return
        if (resultCode != Activity.RESULT_OK) return

        imageUri = data?.data ?: return

        Picasso.with(this)
                .load(imageUri)
                .placeholder(R.drawable.ic_image)
                .fit()
                .into(image)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.image -> chooseImage()
            R.id.uploadButton -> uploadImage()
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_PICK_FILE)
    }

    private fun uploadImage() {
        val uri = imageUri ?: return

        if (!ExternalStoragePermissionActivity.askForPermission(this, uri)) return

        ImgurImageUploader(this, this).uploadImage(uri)
    }

    override fun onSuccess(link: String) {
        Toast.makeText(this, "Upload finished: $link", Toast.LENGTH_SHORT).show()
        Log.d("ImageUpload", "Upload finished: $link")

        copyTextToClipboard("Image link", link)
    }

    override fun onFailure() {
        Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show()
        Log.d("ImageUpload", "Error uploading image")
    }

    companion object {
        private const val REQUEST_PICK_FILE = 1001
    }
}
