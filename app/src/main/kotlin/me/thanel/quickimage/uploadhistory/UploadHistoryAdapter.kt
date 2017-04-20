package me.thanel.quickimage.uploadhistory

import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.thanel.quickimage.R
import me.thanel.quickimage.db.uploadhistory.UploadHistoryTable

class UploadHistoryAdapter : RecyclerView.Adapter<UploadHistoryAdapter.ViewHolder>() {
    private var cursor: Cursor? = null
    private var linkColumnIndex = 0

    fun swapCursor(newCursor: Cursor?) {
        if (newCursor == cursor) return
        cursor?.close()
        cursor = newCursor

        cursor?.let {
            linkColumnIndex = it.getColumnIndexOrThrow(UploadHistoryTable.COLUMN_LINK)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_history, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cursor = cursor ?: return
        if (!cursor.moveToPosition(position)) return

        holder.linkView.text = cursor.getString(linkColumnIndex)
    }

    override fun getItemCount() = cursor?.count ?: 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linkView by lazy { itemView.findViewById(R.id.link) as TextView }
    }
}