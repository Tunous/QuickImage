package me.thanel.quickimage.uploader.imgur

import android.content.Context
import me.thanel.quickimage.uploader.ImageUploader
import me.thanel.quickimage.uploader.imgur.model.ImgurResponse
import me.thanel.quickimage.uploader.imgur.model.UploadedImage
import okhttp3.RequestBody
import retrofit2.Retrofit

/**
 * Uploader which uploads images to the Imgur server.
 */
class ImgurImageUploader(context: Context, callback: Callback) :
        ImageUploader<ImgurResponse<UploadedImage>>(context, callback) {
    override val baseUrl get() = ImgurAPI.BASE_URL

    override fun Retrofit.onExecute(image: RequestBody) =
            create(ImgurAPI::class.java)
                    .uploadImage(ImgurAPI.CLIENT_ID_HEADER, image)

    override fun onResponse(context: Context, response: ImgurResponse<UploadedImage>) =
            if (response.success) response.data.link else null
}