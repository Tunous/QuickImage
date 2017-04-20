package me.thanel.quickimage.db.uploadhistory

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.widget.Toast

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

    private const val SERVICE_IMGUR = 0

    const val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
            "$COLUMN_ID TEXT, " +
            "$COLUMN_SERVICE INTEGER, " +
            "$COLUMN_LINK TEXT NOT NULL" +
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
        }

        context.contentResolver.insert(UploadHistoryProvider.CONTENT_URI, cv)?.let {
            Toast.makeText(context, "Link saved to history", Toast.LENGTH_SHORT).show()
        }
    }
}