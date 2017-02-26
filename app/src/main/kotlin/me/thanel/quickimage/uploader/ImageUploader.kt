package me.thanel.quickimage.uploader

import android.content.Context
import android.net.Uri
import me.thanel.quickimage.DocumentHelper
import me.thanel.quickimage.isConnected
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.lang.ref.WeakReference

/**
 * Base class for image uploaders.
 */
abstract class ImageUploader<ResponseModel>(context: Context, private val callback: Callback) :
        Callback<ResponseModel> {
    private val contextReference = WeakReference(context)

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
    protected abstract fun onResponse(context: Context, response: ResponseModel): String?

    /**
     * Callback to be executed on success or failure of image upload.
     */
    interface Callback {
        /**
         * Called when the image has been successfully uploaded.
         *
         * @param link the link to the uploaded image
         */
        fun onSuccess(link: String)

        /**
         * Called if an error happened when trying to upload the image.
         */
        fun onFailure()
    }

    /**
     * Upload the image to the server.
     *
     * @param fileUri the uri of the image file to upload.
     */
    fun uploadImage(fileUri: Uri) {
        val context = contextReference.get()
        if (context == null || !context.isConnected) {
            notifyFailure()
            return
        }

        val filePath = DocumentHelper.getPath(context, fileUri)
        if (filePath.isNullOrEmpty()) {
            notifyFailure()
            return
        }

        val file = File(filePath)

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        val image = RequestBody.create(
                MediaType.parse(context.contentResolver.getType(fileUri)),
                file)

        retrofit.onExecute(image).enqueue(this)
    }


    final override fun onResponse(call: Call<ResponseModel>,
                                  response: Response<ResponseModel>) {
        val context = contextReference.get() ?: return
        if (!response.isSuccessful) {
            notifyFailure()
            return
        }

        val link = onResponse(context, response.body())
        if (link != null) {
            notifySuccess(link)
        } else {
            notifyFailure()
        }
    }

    final override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
        t.printStackTrace()
        notifyFailure()
    }

    private fun notifySuccess(link: String) {
        callback.onSuccess(link)
    }

    private fun notifyFailure() {
        callback.onFailure()
    }
}
