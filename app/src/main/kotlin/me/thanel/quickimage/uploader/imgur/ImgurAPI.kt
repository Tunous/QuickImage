package me.thanel.quickimage.uploader.imgur

import me.thanel.quickimage.BuildConfig
import me.thanel.quickimage.uploader.imgur.model.ImgurResponse
import me.thanel.quickimage.uploader.imgur.model.UploadedImage
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

internal interface ImgurAPI {
    /**
     * Upload an image to Imgur.
     *
     * @param auth type of authorization for upload
     * @param file a binary file, base64 data, or a URL for an image (up to 10MB)
     */
    @Multipart
    @POST("image")
    fun uploadImage(
            @Header("Authorization") auth: String,
            @Part("image") file: RequestBody
    ): Call<ImgurResponse<UploadedImage>>

    companion object {
        const val BASE_URL = "https://api.imgur.com/3/"
        const val CLIENT_ID_HEADER = "Client-ID ${BuildConfig.IMGUR_API_CLIENT_ID}"
    }
}