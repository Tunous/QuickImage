package me.thanel.quickimage.uploadhistory

import android.content.Intent

object UploadHistoryContract {
    interface View {
        fun onStartActivity(intent: Intent)
        fun onItemClick(view: android.view.View)
    }

    interface Presenter {
        fun viewImage(link: String)
    }
}
