package me.thanel.quickimage.db.uploadhistory

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import java.util.*

/**
 * Definition of table for history of uploaded images.
 */
object UploadHistoryTable : BaseColumns {
    const val TABLE_NAME = "UploadHistory"

    /**
     * The id of the uploaded image.
     */
    const val COLUMN_ID = "id"

    /**
     * The service used to upload the image.
     */
    const val COLUMN_SERVICE = "service"

    /**
     * The link to the uploaded image.
     */
    const val COLUMN_LINK = "link"

    /**
     * The time when the image was uploaded.
     */
    const val COLUMN_TIMESTAMP = "timestamp"

    private const val SERVICE_IMGUR = 0

    const val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
            "$COLUMN_ID TEXT, " +
            "$COLUMN_SERVICE INTEGER NOT NULL, " +
            "$COLUMN_LINK TEXT NOT NULL, " +
            "$COLUMN_TIMESTAMP INTEGER NOT NULL" +
            ")"

    /**
     * Save a link to the upload history table and notify the user about success through a toast
     * message.
     */
    fun saveLink(context: Context, link: String) {
        val cv = ContentValues().apply {
            put(COLUMN_ID, "???")
            put(COLUMN_SERVICE, SERVICE_IMGUR)
            put(COLUMN_LINK, link)
            put(COLUMN_TIMESTAMP, Date().time)
        }

        context.contentResolver.insert(UploadHistoryProvider.CONTENT_URI, cv)
    }

    /**
     * Delete the whole upload history.
     */
    fun deleteAll(context: Context) {
        context.contentResolver.delete(UploadHistoryProvider.CONTENT_URI, null, null)
    }
}