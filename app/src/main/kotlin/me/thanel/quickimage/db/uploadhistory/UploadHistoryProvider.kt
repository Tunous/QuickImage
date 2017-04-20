package me.thanel.quickimage.db.uploadhistory

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.provider.BaseColumns
import android.util.Log
import me.thanel.quickimage.db.DbHelper

class UploadHistoryProvider : ContentProvider() {
    private lateinit var dbHelper: DbHelper

    override fun onCreate(): Boolean {
        dbHelper = DbHelper(context)
        return true
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (uriMatcher.match(uri) != MATCH_ALL) return null

        val db = dbHelper.writableDatabase
        val rowId = db.insert(UploadHistoryTable.TABLE_NAME, null, values)
        if (rowId <= 0) return null

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return ContentUris.withAppendedId(CONTENT_URI, rowId)
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?,
            selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        val queryBuilder = SQLiteQueryBuilder()
        val match = uriMatcher.match(uri)

        queryBuilder.tables = UploadHistoryTable.TABLE_NAME

        when (match) {
            MATCH_ALL -> {
                // no-op
            }
            MATCH_ID -> {
                queryBuilder.appendWhere("${BaseColumns._ID} = ${uri.lastPathSegment}")
            }
            else -> {
                Log.e(UploadHistoryProvider::class.java.simpleName, "query: invalid request: $uri")
                return null
            }
        }

        val db = dbHelper.readableDatabase
        val cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null,
                sortOrder)

        cursor?.setNotificationUri(context.contentResolver, uri)

        return cursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
            selectionArgs: Array<out String>?): Int {
        val match = uriMatcher.match(uri)
        val db = dbHelper.writableDatabase

        val count = when (match) {
            MATCH_ALL -> {
                db.update(UploadHistoryTable.TABLE_NAME, values, selection, selectionArgs)
            }
            MATCH_ID -> {
                if (selection != null && selectionArgs != null) {
                    throw UnsupportedOperationException("Cannot update URI $uri with a where clause")
                }
                db.update(UploadHistoryTable.TABLE_NAME, values, "${BaseColumns._ID} = ?",
                        arrayOf(uri.lastPathSegment))
            }
            else -> {
                throw UnsupportedOperationException("Cannot update URI: $uri")
            }
        }

        if (count > 0) {
            context?.contentResolver?.notifyChange(CONTENT_URI, null)
        }
        return count
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val match = uriMatcher.match(uri)
        val db = dbHelper.writableDatabase

        val count = when (match) {
            MATCH_ALL -> {
                db.delete(UploadHistoryTable.TABLE_NAME, selection, selectionArgs)
            }
            MATCH_ID -> {
                if (selection != null && selectionArgs != null) {
                    throw UnsupportedOperationException("Cannot delete URI $uri with a where clause")
                }
                db.delete(UploadHistoryTable.TABLE_NAME, "${BaseColumns._ID} = ?",
                        arrayOf(uri.lastPathSegment))
            }
            else -> {
                throw UnsupportedOperationException("Cannot delete URI: $uri")
            }
        }

        if (count > 0) {
            context?.contentResolver?.notifyChange(CONTENT_URI, null)
        }
        return count
    }

    companion object {
        private const val MATCH_ALL = 0
        private const val MATCH_ID = 1

        val CONTENT_URI: Uri = Uri.parse("content://me.thanel.quickimage/history")

        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI("me.thanel.quickimage", "history", MATCH_ALL)
            addURI("me.thanel.quickimage", "history/#", MATCH_ID)
        }
    }
}
