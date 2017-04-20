package me.thanel.quickimage.uploadhistory.model

import android.database.Cursor
import android.text.format.DateUtils
import me.thanel.quickimage.db.uploadhistory.UploadHistoryTable
import java.util.*

data class UploadHistoryItem(val link: String, val timestamp: Long) {
    fun timestampString(currentTime: Long) =
            DateUtils.getRelativeTimeSpanString(timestamp, currentTime,
                    DateUtils.MINUTE_IN_MILLIS, 0).toString()

    fun wasUploadedAtSimilarTime(otherItem: UploadHistoryItem?): Boolean {
        val currentTime = Date().time
        return timestampString(currentTime) == otherItem?.timestampString(currentTime)
    }

    companion object {
        fun fromCursor(cursor: Cursor) = UploadHistoryItem(
                cursor.getString(cursor.getColumnIndex(UploadHistoryTable.COLUMN_LINK)),
                cursor.getLong(cursor.getColumnIndex(UploadHistoryTable.COLUMN_TIMESTAMP))
        )
    }
}