package me.thanel.quickimage.uploadhistory

import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_history.view.*
import me.thanel.quickimage.R
import me.thanel.quickimage.db.uploadhistory.UploadHistoryTable

class UploadHistoryAdapter(
        private val presenter: UploadHistoryContract.View
) : RecyclerView.Adapter<UploadHistoryAdapter.ViewHolder>(), View.OnClickListener {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.row_history, parent, false).apply {
            setOnClickListener(this@UploadHistoryAdapter)
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cursor = cursor ?: return
        if (!cursor.moveToPosition(position)) return

        val link = cursor.getString(linkColumnIndex)
        holder.itemView.tag = link
        holder.linkView.text = link

        holder.imageView.apply {
            Picasso.with(context)
                    .load(link)
                    .placeholder(R.drawable.ic_image_black)
                    .error(R.drawable.ic_alert_black)
                    .into(this)
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.imageView.apply {
            Picasso.with(context)
                    .cancelRequest(this)
        }
    }

    override fun getItemCount() = cursor?.count ?: 0

    override fun onClick(v: View) {
        presenter.onItemClick(v)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linkView: TextView by lazy { itemView.link }
        val imageView: ImageView by lazy { itemView.image }
    }
}