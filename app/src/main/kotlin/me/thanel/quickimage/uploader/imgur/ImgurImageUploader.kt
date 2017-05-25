package me.thanel.quickimage.uploader.imgur

import android.content.Context
import me.thanel.quickimage.uploader.ImageUploader
import me.thanel.quickimage.uploader.imgur.model.ImgurResponse
import me.thanel.quickimage.uploader.imgur.model.UploadedImage
import okhttp3.RequestBody
import retrofit2.Retrofit

class ImgurImageUploader(context: Context) : ImageUploader<ImgurResponse<UploadedImage>>(context) {
    override val baseUrl get() = ImgurAPI.BASE_URL

    override fun Retrofit.onExecute(image: RequestBody) =
            create(ImgurAPI::class.java)
                    .uploadImage(ImgurAPI.CLIENT_ID_HEADER, image)

    override fun onResponse(response: ImgurResponse<UploadedImage>) =
            if (response.success) response.data.link else null
}