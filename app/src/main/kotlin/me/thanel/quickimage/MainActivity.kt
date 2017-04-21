package me.thanel.quickimage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import me.thanel.quickimage.settings.SettingsActivity
import me.thanel.quickimage.uploader.imgur.ImgurImageUploader

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PreferenceManager.setDefaultValues(this, R.xml.settings, false)

        uploadButton.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
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

        ImgurImageUploader(this).uploadImage(imageUri)
    }

    companion object {
        private const val REQUEST_PICK_FILE = 1001
    }
}
