package me.thanel.quickimage.uploadhistory.model

import android.database.Cursor
import me.thanel.quickimage.db.uploadhistory.UploadHistoryTable

data class UploadHistoryItem(val link: String) {
    companion object {
        fun fromCursor(cursor: Cursor) = UploadHistoryItem(
                cursor.getString(cursor.getColumnIndex(UploadHistoryTable.COLUMN_LINK))
        )
    }
}