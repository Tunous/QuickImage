package me.thanel.quickimage.uploadhistory

import android.database.Cursor
import android.provider.BaseColumns
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
import me.thanel.quickimage.uploadhistory.model.UploadHistoryItem

class UploadHistoryAdapter(
        private val presenter: UploadHistoryContract.View
) : RecyclerView.Adapter<UploadHistoryAdapter.ViewHolder>(), View.OnClickListener {
    private var cursor: Cursor? = null
    private var linkColumnIndex = 0

    init {
        setHasStableIds(true)
    }

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
        cursor?.let {
            if (it.moveToPosition(position)) {
                holder.bind(UploadHistoryItem.fromCursor(it))
            }
        }
    }

    override fun onViewRecycled(holder: ViewHolder) = holder.unbind()

    override fun getItemCount() = cursor?.count ?: 0

    override fun getItemId(position: Int): Long {
        cursor?.let {
            if (it.moveToPosition(position)) {
                return it.getLong(it.getColumnIndex(BaseColumns._ID))
            }
        }
        return super.getItemId(position)
    }

    override fun onClick(v: View) = presenter.onItemClick(v)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val linkView: TextView by lazy { itemView.link }
        private val imageView: ImageView by lazy { itemView.image }

        fun bind(item: UploadHistoryItem) {
            itemView.tag = item
            linkView.text = item.link

            imageView.apply {
                Picasso.with(context)
                        .load(item.link)
                        .placeholder(R.drawable.ic_image_black)
                        .error(R.drawable.ic_alert_black)
                        .into(this)
            }
        }

        fun unbind() {
            imageView.apply {
                Picasso.with(context)
                        .cancelRequest(this)
            }
        }
    }
}