package me.thanel.quickimage

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

/**
 * Activity which shows the user a dialog to grant the permission to access external storage.
 */
class ExternalStoragePermissionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = ""

        requireExternalStoragePermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
            grantResults: IntArray) {
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val imageUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                if (imageUri != null) {
                    val uploadIntent = UploadActivity.getUploadImageIntent(this, imageUri)
                    startActivity(uploadIntent)
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

        finish()
    }

    private fun requireExternalStoragePermission() {
        if (hasPermission(this)) {
            finish()
            return
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            AlertDialog.Builder(this)
                    .setTitle(R.string.permission_read_external_storage_title)
                    .setMessage(R.string.permission_read_external_storage_message)
                    .setPositiveButton(R.string.grant_permission, { _, _ ->
                        requestReadExternalStoragePermission()
                    })
                    .setNegativeButton(R.string.cancel, { _, _ ->
                        finish()
                    })
                    .show()
        } else {
            requestReadExternalStoragePermission()
        }
    }

    private fun requestReadExternalStoragePermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_READ_EXTERNAL_STORAGE)
    }

    companion object {
        private const val REQUEST_READ_EXTERNAL_STORAGE = 1115

        /**
         * Ask the user to grant the permission to access external storage.
         * If the permission has been already granted this method does nothing, otherwise it
         * launches the [ExternalStoragePermissionActivity] which in turn will show the permission
         * dialog.
         *
         * @param imageUri The uri of the image that should be automatically uploaded once the
         * permission is granted. Note: It is only used if the permission wasn't granted previously.
         *
         * @return `true` if the permission is already granted, `false` otherwise.
         */
        fun askForPermission(context: Context, imageUri: Uri? = null): Boolean {
            if (hasPermission(context)) return true

            val intent = Intent(context, ExternalStoragePermissionActivity::class.java).apply {
                imageUri?.let { putExtra(Intent.EXTRA_STREAM, imageUri) }
            }
            context.startActivity(intent)
            return false
        }

        private fun hasPermission(context: Context): Boolean {
            val permissionStatus = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
            return permissionStatus == PackageManager.PERMISSION_GRANTED
        }
    }
}