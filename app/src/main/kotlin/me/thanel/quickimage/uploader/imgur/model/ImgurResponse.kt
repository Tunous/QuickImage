package me.thanel.quickimage.uploader.imgur.model

/**
 * Wrapper for responses from Imgur.
 *
 * [See documentation](https://api.imgur.com/models)
 *
 * @property success Tells whether the request was successful.
 * @property status The HTTP status code.
 * @property data Mixed data, different for various API endpoints.
 */
data class ImgurResponse<out T : ImgurResponseData>(
        val success: Boolean,
        val status: Int,
        val data: T)
