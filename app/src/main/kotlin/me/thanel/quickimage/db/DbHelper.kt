package me.thanel.quickimage.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import me.thanel.quickimage.db.uploadhistory.UploadHistoryTable

/**
 * Helper for managing the QuickImage database.
 */
class DbHelper(context: Context) : SQLiteOpenHelper(context, "QuickImage.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        // Create the table for storing history of uploaded images
        db.execSQL(UploadHistoryTable.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }
}