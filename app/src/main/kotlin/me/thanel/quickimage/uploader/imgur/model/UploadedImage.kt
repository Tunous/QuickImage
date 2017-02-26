package me.thanel.quickimage.uploader.imgur.model

/**
 * The base model for an image.
 *
 * http://api.imgur.com/models/image
 *
 * @property id The ID for the image.
 * @property link The direct link to the the image.
 */
data class UploadedImage(val id: String, val link: String)