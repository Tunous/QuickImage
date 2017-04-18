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

/**
 * [See documentation](https://api.imgur.com/)
 */
internal interface ImgurAPI {
    /**
     * Upload an image to Imgur.
     *
     * [See documentation](https://api.imgur.com/endpoints/image#image-upload)
     *
     * @param auth Type of authorization for upload.
     * @param image A binary file, base64 data, or a URL for an image. (up to 10MB)
     */
    @Multipart
    @POST("image")
    fun uploadImage(
            @Header("Authorization") auth: String,
            @Part("image") image: RequestBody
    ): Call<ImgurResponse<UploadedImage>>

    companion object {
        const val BASE_URL = "https://api.imgur.com/3/"
        const val CLIENT_ID_HEADER = "Client-ID ${BuildConfig.IMGUR_API_CLIENT_ID}"
    }
}