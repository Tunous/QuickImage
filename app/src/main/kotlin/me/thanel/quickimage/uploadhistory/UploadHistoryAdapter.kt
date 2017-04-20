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
import java.util.*

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
            historyCard.setOnClickListener(this@UploadHistoryAdapter)
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cursor?.let {
            if (it.moveToPosition(position)) {
                val item = UploadHistoryItem.fromCursor(it)
                val previousItem = if (it.position > 0 && it.moveToPrevious()) {
                    UploadHistoryItem.fromCursor(it)
                } else {
                    null
                }

                it.moveToPosition(position)

                val addBottomMargin = it.isLast || it.moveToNext() &&
                        !UploadHistoryItem.fromCursor(it).wasUploadedAtSimilarTime(item)

                holder.bind(item, previousItem, addBottomMargin)
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
        private val linkView by lazy { itemView.findViewById(R.id.link) as TextView }
        private val imageView by lazy { itemView.findViewById(R.id.image) as ImageView }
        private val dateHeader by lazy { itemView.findViewById(R.id.dateHeader) as TextView }
        private val historyCard by lazy { itemView.findViewById(R.id.historyCard) }

        fun bind(item: UploadHistoryItem, previousItem: UploadHistoryItem?,
                addBottomMargin: Boolean) {
            historyCard.tag = item
            linkView.text = item.link

            dateHeader.visibility = if (!item.wasUploadedAtSimilarTime(previousItem)) {
                View.VISIBLE
            } else {
                View.GONE
            }

            historyCard.apply {
                layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
                    val bottomMargin = if (addBottomMargin) {
                        context.resources.getDimensionPixelSize(R.dimen.card_section_bottom_margin)
                    } else {
                        1
                    }
                    setMargins(0, 0, 0, bottomMargin)
                }
            }

            dateHeader.text = item.timestampString(Date().time)

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