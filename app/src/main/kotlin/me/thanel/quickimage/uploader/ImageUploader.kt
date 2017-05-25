package me.thanel.quickimage.uploader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.preference.PreferenceManager
import android.widget.Toast
import me.thanel.quickimage.R
import me.thanel.quickimage.db.uploadhistory.UploadHistoryTable
import me.thanel.quickimage.extensions.*
import me.thanel.quickimage.settings.SettingsFragment
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.lang.ref.WeakReference

/**
 * Base class for image uploaders.
 */
abstract class ImageUploader<ResponseModel>(context: Context) {
    private val contextReference = WeakReference(context)

    private var notificationId = 0

    /**
     * The API base URL.
     */
    protected abstract val baseUrl: String

    /**
     * Create an Retrofit [Call] object which will be used to perform image upload request.
     */
    protected abstract fun Retrofit.onExecute(image: RequestBody): Call<ResponseModel>

    /**
     * Handle the response from the API endpoint.
     *
     * @return The link to the uploaded image, or null if upload failed.
     */
    abstract fun onResponse(response: ResponseModel): String?

    /**
     * Upload the image to the server.
     *
     * @param fileUri the uri of the image file to upload.
     */
    fun uploadImage(fileUri: Uri) {
        val id = notificationId
        notificationId += 1

        val context = contextReference.get()
        if (context == null || !context.isConnected) {
            notifyFailure(id)
            return
        }

        val filePath = DocumentHelper.getPath(context, fileUri)
        if (filePath.isNullOrEmpty()) {
            notifyFailure(id)
            return
        }

        context.createUploadingNotification(id)

        val file = File(filePath)
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        val image = RequestBody.create(
                MediaType.parse(context.contentResolver.getType(fileUri)),
                file)

        val call = UploadCall(this, id, file.absolutePath)
        retrofit.onExecute(image).enqueue(call)
    }

    fun notifySuccess(id: Int, link: String, filePath: String) {
        contextReference.get()?.let { context ->
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val showNotification =
                    preferences.getBoolean(SettingsFragment.KEY_PREF_SUCCESS_NOTIFICATION, true)
            if (showNotification) {
                val options = BitmapFactory.Options().apply {
                    inPreferredConfig = Bitmap.Config.ARGB_8888
                }
                val image = BitmapFactory.decodeFile(filePath, options)

                context.createUploadedNotification(id, link, image)
            } else {
                context.hideUploadNotification(id)
            }

            if (preferences.getBoolean(SettingsFragment.KEY_PREF_AUTOMATIC_COPY, true)) {
                context.copyTextToClipboard("Image link", link)
                Toast.makeText(context, R.string.copied_link, Toast.LENGTH_SHORT).show()
            }

            UploadHistoryTable.saveLink(context, link)
        }
    }

    fun notifyFailure(id: Int) {
        contextReference.get()?.createFailedUploadNotification(id)
    }
}
