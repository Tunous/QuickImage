package me.thanel.quickimage.uploadhistory

import android.content.Intent
import android.net.Uri

class UploadHistoryPresenter(
        private val view: UploadHistoryContract.View
) : UploadHistoryContract.Presenter {
    override fun viewImage(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        view.onStartActivity(intent)
    }
}