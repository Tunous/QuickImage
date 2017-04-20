package me.thanel.quickimage.uploadhistory

import android.content.Intent
import me.thanel.quickimage.uploadhistory.model.UploadHistoryItem

object UploadHistoryContract {
    interface View {
        fun onStartActivity(intent: Intent)
        fun onItemClick(view: android.view.View)
    }

    interface Presenter {
        fun viewHistoryItem(item: UploadHistoryItem)
    }
}
