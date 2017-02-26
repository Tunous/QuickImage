package me.thanel.quickimage.uploader.imgur.model

/**
 * Response from Imgur when uploading to the server.
 */
data class ImgurResponse<out T>(val success: Boolean, val status: Int, val data: T)
