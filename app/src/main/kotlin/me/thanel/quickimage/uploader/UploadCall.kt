package me.thanel.quickimage.uploader

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadCall<ResponseModel>(
        private val uploader: ImageUploader<ResponseModel>,
        private val notificationId: Int,
        private val filePath: String
) : Callback<ResponseModel> {

    override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
        if (!response.isSuccessful) {
            uploader.notifyFailure(notificationId)
            return
        }

        val link = uploader.onResponse(response.body())
        if (link != null) {
            uploader.notifySuccess(notificationId, link, filePath)
        } else {
            uploader.notifyFailure(notificationId)
        }
    }

    override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
        t.printStackTrace()
        uploader.notifyFailure(notificationId)
    }
}