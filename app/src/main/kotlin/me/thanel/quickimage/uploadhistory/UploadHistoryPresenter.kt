package me.thanel.quickimage.uploadhistory

import android.content.Intent
import android.net.Uri
import me.thanel.quickimage.uploadhistory.model.UploadHistoryItem

class UploadHistoryPresenter(
        private val view: UploadHistoryContract.View
) : UploadHistoryContract.Presenter {
    override fun viewHistoryItem(item: UploadHistoryItem) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
        view.onStartActivity(intent)
    }
}